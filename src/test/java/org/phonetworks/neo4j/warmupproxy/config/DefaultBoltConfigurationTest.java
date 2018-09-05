package org.phonetworks.neo4j.warmupproxy.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.phonetworks.neo4j.warmupproxy.binding.URIConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DefaultBoltConfigurationTest.TestConfiguration.class)
public class DefaultBoltConfigurationTest {

    @Autowired
    private BoltConfiguration config;

    @Test
    public void test() {
        assertNotNull(config);
        assertNotNull(config.getConnectionConfigs());
        assertEquals(1, config.getConnectionConfigs().size());
    }

    @EnableConfigurationProperties(BoltConfiguration.class)
    public static class TestConfiguration {

        @Bean
        @ConfigurationPropertiesBinding
        URIConverter instantiateUriConverter() {
            return new URIConverter();
        }
    }
}
