package org.phonetworks.neo4j.warmupproxy;

import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.phonetworks.neo4j.warmupproxy.bolt.BoltClient;
import org.phonetworks.neo4j.warmupproxy.bolt.BoltClientConfig;
import org.phonetworks.neo4j.warmupproxy.bolt.BoltClientFactory;
import org.phonetworks.neo4j.warmupproxy.config.BoltConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class Aggregator {

    private final Logger logger = LoggerFactory.getLogger(Aggregator.class);

    private final BoltConfiguration boltConfig;

    private final KeyedObjectPool<BoltClientConfig, BoltClient> clients;

    private final ExecutorService executorService;

    public Aggregator(@Autowired BoltConfiguration boltConfig, @Autowired BoltClientFactory factory) {
        this.boltConfig = boltConfig;

        GenericKeyedObjectPoolConfig<BoltClient> config = new GenericKeyedObjectPoolConfig<>();

        clients = new GenericKeyedObjectPool<>(factory, config);

        executorService = Executors.newFixedThreadPool(boltConfig.getExecutorPoolSize());
    }

    @NonNull
    public List<Map<String, Object>> run(@NonNull String cypherQuery) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();

        logger.debug("run " + cypherQuery);

        if (boltConfig.getConnectionConfigs().isEmpty()) {
            logger.warn("no neo4j servers in config");
            return result;
        }

        List<CypherRunThread> tasks = new ArrayList<>();
        for (BoltClientConfig config : boltConfig.getConnectionConfigs()) {
            tasks.add(new CypherRunThread(config, cypherQuery));
        }

        // https://stackoverflow.com/questions/3269445/executorservice-how-to-wait-for-all-tasks-to-finish/3269572
        List<Future<List<Map<String, Object>>>> futures = executorService.invokeAll(tasks,
                boltConfig.getExecutionTimeoutMillis(), TimeUnit.MILLISECONDS);

        for (int i = 0; i < futures.size(); i++) {
            BoltClientConfig config = tasks.get(i).getConfig();

            try {
                Future<List<Map<String, Object>>> future = futures.get(i);
                List<Map<String, Object>> single = future.get();
                result.addAll(single);
            } catch (Exception e) {
                logger.error("cannot process " + cypherQuery + " for " + config, e);
            }
        }

        return result;
    }

    private final class CypherRunThread implements Callable<List<Map<String, Object>>> {
        private final BoltClientConfig config;
        private final String cypherQuery;

        CypherRunThread(BoltClientConfig config, String cypherQuery) {
            this.config = config;
            this.cypherQuery = cypherQuery;
        }

        public BoltClientConfig getConfig() {
            return config;
        }

        @Override
        public List<Map<String, Object>> call() throws Exception {
            BoltClient client = null;

            try {
                client = clients.borrowObject(config);

                return client.run(cypherQuery);
            } catch (Exception e) {
                logger.error("borrowObject", e);
                throw e;
            } finally {
                if (client != null) {
                    try {
                        clients.returnObject(config, client);
                    } catch (Exception e) {
                        logger.error("returnObject", e);
                    }
                }
            }
        }
    }
}
