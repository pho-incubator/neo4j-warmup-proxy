package org.phonetworks.neo4j.warmupproxy.bolt;

import org.neo4j.driver.v1.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class Neo4jClient implements BoltClient {
    private Driver driver;
    private BoltClientConfig config;

    @Override
    public void connect(@NonNull BoltClientConfig config) {
        URI serverUri = config.getUri();

        String userInfo = serverUri.getUserInfo();
        if (userInfo == null) {
            throw new IllegalArgumentException("Input URI have missing userInfo");
        }

        String[] userInfoComponents = userInfo.split(":");
        if (userInfoComponents.length < 2) {
            throw new IllegalArgumentException("Invalid userInfo in URI '" + userInfo + "'");
        }

        this.config = config;

        // TODO pass org.neo4j.driver.v1.Config based on org.phonetworks.neo4j.warmupproxy.bolt.BoltClientConfig
        this.driver = GraphDatabase.driver(serverUri,
                AuthTokens.basic(userInfoComponents[0], userInfoComponents[1]));
    }

    @Override
    public List<Map<String, Object>> run(@NonNull String cypherQuery) {
        Session session = driver.session();

        StatementResult resultSet = session.beginTransaction().run(cypherQuery);
        List<Map<String, Object>> result = resultSet.list(Record::asMap);

        session.close();

        return result;
    }

    @Override
    public @Nullable BoltClientConfig getConfig() {
        return config;
    }

    @Override
    public void disconnect() {
        driver.close();
    }
}
