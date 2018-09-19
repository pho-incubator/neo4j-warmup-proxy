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

@Component
public class Aggregator {

    private final Logger logger = LoggerFactory.getLogger(Aggregator.class);

    private final BoltConfiguration boltConfig;

    private final KeyedObjectPool<BoltClientConfig, BoltClient> clients;

    public Aggregator(@Autowired BoltConfiguration boltConfig, @Autowired BoltClientFactory factory) {
        this.boltConfig = boltConfig;

        GenericKeyedObjectPoolConfig<BoltClient> config = new GenericKeyedObjectPoolConfig<>();

        clients = new GenericKeyedObjectPool<>(factory, config);
    }

    @NonNull
    public List<Map<String, Object>> run(@NonNull String cypherQuery) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();

        logger.debug("run " + cypherQuery);

        // TODO do it in parallel if performance not enough
        // https://stackoverflow.com/questions/3269445/executorservice-how-to-wait-for-all-tasks-to-finish/3269572
        for (BoltClientConfig config : boltConfig.getConnectionConfigs()) {
            BoltClient client = null;

            try {
                client = clients.borrowObject(config);

                List<Map<String, Object>> single = client.run(cypherQuery);

                result.addAll(single);
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

        if (boltConfig.getConnectionConfigs().isEmpty()) {
            logger.warn("No neo4j servers in config");
        }

        return result;
    }
}
