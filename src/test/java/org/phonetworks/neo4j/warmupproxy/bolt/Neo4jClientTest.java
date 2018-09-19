package org.phonetworks.neo4j.warmupproxy.bolt;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.neo4j.graphdb.GraphDatabaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URI;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext // important to shutdown previous instances of GraphDatabaseService
@SpringBootTest(classes = Neo4jEmbeddedServerConfiguration.class)
public class Neo4jClientTest {

    @Autowired
    private GraphDatabaseService neo4jServer;

    @Test
    public void testFailSafeConstruction() {
        new Neo4jClient();
    }

    @Test
    public void testConnectionSuccess() throws Exception {
        BoltClientConfig config = new BoltClientConfig();

        config.setUri(Neo4jEmbeddedServerConfiguration.TEST);

        Neo4jClient client = new Neo4jClient();
        client.connect(config);
    }

    @Test(expected = org.neo4j.driver.v1.exceptions.ServiceUnavailableException.class)
    public void testConnectionServerNotStarted() throws Exception {
        BoltClientConfig config = new BoltClientConfig();
        config.setUri(new URI(Neo4jEmbeddedServerConfiguration.TEST.getScheme(),
                Neo4jEmbeddedServerConfiguration.TEST.getUserInfo(),
                Neo4jEmbeddedServerConfiguration.TEST.getHost(),
                11111, // not existing port
                Neo4jEmbeddedServerConfiguration.TEST.getPath(),
                Neo4jEmbeddedServerConfiguration.TEST.getQuery(),
                Neo4jEmbeddedServerConfiguration.TEST.getFragment()));
        config.setEncrypted(false);

        Neo4jClient client = new Neo4jClient();
        client.connect(config);
    }

}
