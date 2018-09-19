package org.phonetworks.neo4j.warmupproxy;

import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.phonetworks.neo4j.warmupproxy.bolt.BoltClientConfig;
import org.phonetworks.neo4j.warmupproxy.bolt.BoltClientFactory;
import org.phonetworks.neo4j.warmupproxy.bolt.Neo4jClient;
import org.phonetworks.neo4j.warmupproxy.config.BoltConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class) // https://stackoverflow.com/a/44108517/902217
@SpringBootTest(classes = Aggregator.class)
public class AggregatorTest {
    @Mock
    private BoltConfiguration boltConfigMock;

    @Mock
    private Neo4jClient neo4jClient;

    @Mock
    private BoltClientFactory factoryMock;

    private Aggregator aggregator;

    @Before
    public void setUp() {
        Mockito.when(boltConfigMock.getClientsLimit()).thenReturn(5);
        aggregator = new Aggregator(boltConfigMock, factoryMock);
    }

    @Test
    public void testNoServers() throws Exception {
        Mockito.when(boltConfigMock.getConnectionConfigs()).thenReturn(Collections.emptyList());
        List<?> result = aggregator.run("MATCH(n) RETURN n;");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testOneServer() throws Exception {
        BoltClientConfig config = new BoltClientConfig();
        Mockito.when(boltConfigMock.getConnectionConfigs()).thenReturn(
                Collections.singletonList(config));
        Mockito.when(factoryMock.makeObject(Mockito.eq(config))).thenReturn(
                new DefaultPooledObject<>(neo4jClient));
        Mockito.when(neo4jClient.run(Mockito.anyString())).thenReturn(
                Collections.singletonList(Collections.singletonMap("key", "value")));

        List<?> result = aggregator.run("MATCH(n) RETURN n;");
        assertEquals(1, result.size());
    }
}
