package org.phonetworks.neo4j.warmupproxy.config;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ExternalServerConfigurationTest.TestConfiguration.class)
@ActiveProfiles("test")
public class ExternalServerConfigurationTest {

    @Autowired
    private ServerConfiguration config;

    @Test
    public void test() {
        assertNotNull(config);
        assertEquals(6666, config.getSocketPort());
        assertEquals(10000, config.getSocketTimeoutMillis());
        assertTrue(config.isSocketKeepAlive());
    }

    @EnableConfigurationProperties(ServerConfiguration.class)
    @TestPropertySource(locations = "classpath:application-test.yml")
    public static class TestConfiguration {
        // nothing
    }
}
