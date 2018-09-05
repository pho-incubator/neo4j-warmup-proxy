package org.phonetworks.neo4j.warmupproxy;

import org.phonetworks.neo4j.warmupproxy.bolt.BoltClient;
import org.phonetworks.neo4j.warmupproxy.bolt.BoltClientConfig;
import org.phonetworks.neo4j.warmupproxy.bolt.BoltClientManager;
import org.phonetworks.neo4j.warmupproxy.config.BoltConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class Aggregator {

    @Autowired
    private BoltConfiguration boltConfig;

    @Autowired
    private BoltClientManager factory;

    public List<Map<String, Object>> run(String cypherQuery) {
        List<Map<String, Object>> result = new ArrayList<>();

        // TODO do it in parallel
        for (BoltClientConfig config : boltConfig.getConnectionConfigs()) {
            BoltClient client = factory.getNeo4jClient(config);

            // TODO improve connection managent
            client.connect(config);
            List<Map<String, Object>> single = client.run(cypherQuery);
            client.disconnect();

            result.addAll(single);
        }

        return result;
    }
}
