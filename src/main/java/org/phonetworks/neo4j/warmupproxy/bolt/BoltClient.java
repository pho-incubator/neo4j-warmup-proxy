package org.phonetworks.neo4j.warmupproxy.bolt;

import java.util.List;
import java.util.Map;

public interface BoltClient {
    /**
     * Request resource
     * @param clientConfig to configure connection
     */
    void connect(BoltClientConfig clientConfig);

    /**
     * Execute cypher query on bolt (neo4j) server
     * @param query - request query on cypher language
     * @return query execution result
     *
     * TODO revisit return result type
     */
    List<Map<String, Object>> run(String query);

    /**
     * Release resources
     */
    void disconnect();
}
