package org.phonetworks.neo4j.warmupproxy.bolt;

import org.phonetworks.neo4j.warmupproxy.config.BoltConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BoltClientManager {

    @Autowired
    private BoltConfiguration boltConfig;

    public Map<BoltClientConfig, BoltClient> cache = new HashMap<>();

    public BoltClient getNeo4jClient(BoltClientConfig clientConfig) {
        if (!boltConfig.getConnectionConfigs().contains(clientConfig)) {
            throw new IllegalArgumentException("Unknown input clientConfig=" + clientConfig);
        }

        if (!cache.containsKey(clientConfig)) {
            cache.put(clientConfig, new Neo4jClient());
        }

        return cache.get(clientConfig);
    }
}
