package org.phonetworks.neo4j.warmupproxy.config;

import org.phonetworks.neo4j.warmupproxy.bolt.BoltClientConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * TODO watch for https://github.com/spring-projects/spring-boot/issues/8762 to remove setters later
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "bolt")
public class BoltConfiguration {
    @Min(1)
    private int totalConnectionLimit = 15;

    @Min(1)
    private int executorPoolSize = 10;

    private int executionTimeoutMillis = (int) TimeUnit.SECONDS.toMillis(10);

    private List<BoltClientConfig> connectionConfigs = new ArrayList<>();

    @NonNull
    public List<BoltClientConfig> getConnectionConfigs() {
        return connectionConfigs;
    }

    public void setConnectionConfigs(@NonNull List<BoltClientConfig> connectionConfigs) {
        this.connectionConfigs = connectionConfigs;
    }

    public int getTotalConnectionLimit() {
        return totalConnectionLimit;
    }

    public void setTotalConnectionLimit(int totalConnectionLimit) {
        this.totalConnectionLimit = totalConnectionLimit;
    }

    public int getExecutorPoolSize() {
        return executorPoolSize;
    }

    public void setExecutorPoolSize(int executorPoolSize) {
        this.executorPoolSize = executorPoolSize;
    }

    public int getExecutionTimeoutMillis() {
        return executionTimeoutMillis;
    }

    public void setExecutionTimeoutMillis(int executionTimeoutMillis) {
        this.executionTimeoutMillis = executionTimeoutMillis;
    }
}
