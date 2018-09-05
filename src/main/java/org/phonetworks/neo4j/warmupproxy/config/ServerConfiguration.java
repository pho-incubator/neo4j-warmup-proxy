package org.phonetworks.neo4j.warmupproxy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * TODO watch for https://github.com/spring-projects/spring-boot/issues/8762 to remove setters later
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "server")
public class ServerConfiguration {

    @Min(1024)
    @Max(Short.MAX_VALUE << 1)
    private int socketPort;

    private boolean socketKeepAlive;

    @Min(-1)
    private int socketTimeoutMillis;

    public int getSocketPort() {
        return socketPort;
    }

    public boolean isSocketKeepAlive() {
        return socketKeepAlive;
    }

    public int getSocketTimeoutMillis() {
        return socketTimeoutMillis;
    }

    public void setSocketPort(int socketPort) {
        this.socketPort = socketPort;
    }

    public void setSocketKeepAlive(boolean socketKeepAlive) {
        this.socketKeepAlive = socketKeepAlive;
    }

    public void setSocketTimeoutMillis(int socketTimeoutMillis) {
        this.socketTimeoutMillis = socketTimeoutMillis;
    }
}
