package org.phonetworks.neo4j.warmupproxy.bolt;

import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.server.Bootstrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.*;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext // important to shutdown previous instances of GraphDatabaseService
@SpringBootTest(classes = { Neo4jEmbeddedServerConfiguration.class, BoltClientFactory.class })
public class BoltClientFactoryTest {

    @Rule
    public Timeout timeoutRule = new Timeout(50, TimeUnit.SECONDS);

    @Autowired
    private BoltClientFactory factory;

    @Autowired
    private GraphDatabaseService neo4jServer;

    @Test
    public void testSerial() throws Exception {
        GenericKeyedObjectPoolConfig<BoltClient> config = new GenericKeyedObjectPoolConfig<>();
        config.setMaxTotal(1);

        final GenericKeyedObjectPool<BoltClientConfig, BoltClient> pool = new GenericKeyedObjectPool<>(factory, config);

        BoltClientConfig key = new BoltClientConfig();
        key.setUri(Neo4jEmbeddedServerConfiguration.TEST);
        BoltClient client = pool.borrowObject(key);
        assertNotNull(client);
        pool.returnObject(key, client);
    }

    @Test
    public void testConcurrency() throws Exception {
        GenericKeyedObjectPoolConfig<BoltClient> config = new GenericKeyedObjectPoolConfig<>();
        config.setMaxTotal(5);

        final GenericKeyedObjectPool<BoltClientConfig, BoltClient> pool = new GenericKeyedObjectPool<>(factory, config);

        final BoltClientConfig key = new BoltClientConfig();
        key.setUri(Neo4jEmbeddedServerConfiguration.TEST);

        final int count = 1;
        CountDownLatch countDownLatch = new CountDownLatch(count);

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < count; i++) {
            executorService.submit((Callable<Void>) () -> {
                BoltClient client = null;

                try {
                    client = pool.borrowObject(key);
                    Thread.sleep(200);
                } finally {
                    if (client != null) {
                        pool.returnObject(key, client);
                    }

                    countDownLatch.countDown();
                }

                return null;
            });
        }

        countDownLatch.await();
    }
}
