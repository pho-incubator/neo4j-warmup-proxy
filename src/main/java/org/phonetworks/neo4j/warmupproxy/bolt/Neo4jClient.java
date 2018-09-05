package org.phonetworks.neo4j.warmupproxy.bolt;

import org.neo4j.driver.v1.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class Neo4jClient implements BoltClient {
    private Driver driver;

    @Override
    public void connect(BoltClientConfig clientConfig) {
        URI serverUri = clientConfig.getUri();

        String userInfo = serverUri.getUserInfo();
        if (userInfo == null) {
            throw new IllegalArgumentException("Input URI have missing userInfo");
        }

        String[] userInfoComponents = userInfo.split(":");
        if (userInfoComponents.length < 2) {
            throw new IllegalArgumentException("Invalid userInfo in URI '" + userInfo + "'");
        }

        driver = GraphDatabase.driver(serverUri,
                AuthTokens.basic(userInfoComponents[0], userInfoComponents[1]));
    }

    @Override
    public List<Map<String, Object>> run(String cypherQuery) {
        Session session = driver.session();

        StatementResult resultSet = session.beginTransaction().run(cypherQuery);
        List<Map<String, Object>> result = resultSet.list(Record::asMap);

        session.close();

        return result;
    }

    public void disconnect() {
        driver.close();
    }
}
