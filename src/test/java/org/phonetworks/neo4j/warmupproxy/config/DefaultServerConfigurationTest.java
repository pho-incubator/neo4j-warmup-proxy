package org.phonetworks.neo4j.warmupproxy.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DefaultServerConfigurationTest.TestConfiguration.class)
public class DefaultServerConfigurationTest {

    @Autowired
    private ServerConfiguration config;

    @Test
    public void test() {
        assertNotNull(config);
        assertEquals(9999, config.getSocketPort());
        assertEquals(5000, config.getSocketTimeoutMillis());
        assertFalse(config.isSocketKeepAlive());
    }

    @EnableConfigurationProperties(ServerConfiguration.class)
    public static class TestConfiguration {
        // nothing
    }

}
