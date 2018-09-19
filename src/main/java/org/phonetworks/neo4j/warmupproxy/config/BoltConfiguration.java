package org.phonetworks.neo4j.warmupproxy.config;

import org.phonetworks.neo4j.warmupproxy.bolt.BoltClientConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO watch for https://github.com/spring-projects/spring-boot/issues/8762 to remove setters later
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "bolt")
public class BoltConfiguration {
    @Min(1)
    private int clientsLimit;
    private List<BoltClientConfig> connectionConfigs = new ArrayList<>();

    @NonNull
    public List<BoltClientConfig> getConnectionConfigs() {
        return connectionConfigs;
    }

    public void setConnectionConfigs(@NonNull List<BoltClientConfig> connectionConfigs) {
        this.connectionConfigs = connectionConfigs;
    }

    public int getClientsLimit() {
        return clientsLimit;
    }

    public void setClientsLimit(int clientsLimit) {
        this.clientsLimit = clientsLimit;
    }
}
