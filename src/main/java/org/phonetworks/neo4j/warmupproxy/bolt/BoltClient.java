package org.phonetworks.neo4j.warmupproxy.bolt;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

public interface BoltClient {
    /**
     * Request resource
     * @param clientConfig to configure connection
     */
    void connect(@NonNull BoltClientConfig clientConfig);

    /**
     * Execute cypher query on bolt (neo4j) server
     * @param query - request query on cypher language
     * @return query execution result
     *
     * TODO revisit return result type
     */
    List<Map<String, Object>> run(@NonNull String query);

    /**
     * Get client config
     * @return config
     */
    @Nullable
    BoltClientConfig getConfig();

    /**
     * Release resources
     */
    void disconnect();
}
