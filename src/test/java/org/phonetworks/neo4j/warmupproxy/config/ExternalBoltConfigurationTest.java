package org.phonetworks.neo4j.warmupproxy.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ExternalBoltConfigurationTest.TestConfiguration.class)
@ActiveProfiles("test")
public class ExternalBoltConfigurationTest {

    @Autowired
    private BoltConfiguration config;

    @Test
    public void test() {
        assertNotNull(config);
        assertNotNull(config.getConnectionConfigs());
        assertEquals(2, config.getConnectionConfigs().size());
    }

    @EnableConfigurationProperties(BoltConfiguration.class)
    public static class TestConfiguration {
        // nothing
    }
}
