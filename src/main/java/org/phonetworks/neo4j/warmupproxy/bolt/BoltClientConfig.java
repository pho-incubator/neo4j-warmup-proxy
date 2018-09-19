package org.phonetworks.neo4j.warmupproxy.bolt;

import org.neo4j.driver.internal.net.pooling.PoolSettings;
import org.neo4j.driver.internal.retry.RetrySettings;
import org.neo4j.driver.v1.Config;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Null;
import java.net.URI;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.neo4j.driver.v1.Config.TrustStrategy.trustAllCertificates;

/**
 * TODO watch for https://github.com/spring-projects/spring-boot/issues/8762 to remove setters later
 */
public class BoltClientConfig {
    private URI uri = URI.create("bolt://neo4j:neo4j@localhost:7687");
    private boolean logLeakedSessions = true;
    private boolean encrypted = true;

    private int maxIdleConnectionPoolSize = PoolSettings.DEFAULT_MAX_IDLE_CONNECTION_POOL_SIZE;
    private int idleTimeBeforeConnectionTest = PoolSettings.DEFAULT_IDLE_TIME_BEFORE_CONNECTION_TEST;
    private int routingFailureLimit = 1;
    private int routingRetryDelayMillis = (int) TimeUnit.SECONDS.toMillis( 5 );
    private int connectionTimeoutMillis = (int) TimeUnit.SECONDS.toMillis( 5 );

    public URI getUri() {
        return uri;
    }

    public boolean isLogLeakedSessions() {
        return logLeakedSessions;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public int getMaxIdleConnectionPoolSize() {
        return maxIdleConnectionPoolSize;
    }

    public int getIdleTimeBeforeConnectionTest() {
        return idleTimeBeforeConnectionTest;
    }

    public int getRoutingFailureLimit() {
        return routingFailureLimit;
    }

    public int getRoutingRetryDelayMillis() {
        return routingRetryDelayMillis;
    }

    public int getConnectionTimeoutMillis() {
        return connectionTimeoutMillis;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public void setLogLeakedSessions(boolean logLeakedSessions) {
        this.logLeakedSessions = logLeakedSessions;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public void setMaxIdleConnectionPoolSize(int maxIdleConnectionPoolSize) {
        this.maxIdleConnectionPoolSize = maxIdleConnectionPoolSize;
    }

    public void setIdleTimeBeforeConnectionTest(int idleTimeBeforeConnectionTest) {
        this.idleTimeBeforeConnectionTest = idleTimeBeforeConnectionTest;
    }

    public void setRoutingFailureLimit(int routingFailureLimit) {
        this.routingFailureLimit = routingFailureLimit;
    }

    public void setRoutingRetryDelayMillis(int routingRetryDelayMillis) {
        this.routingRetryDelayMillis = routingRetryDelayMillis;
    }

    public void setConnectionTimeoutMillis(int connectionTimeoutMillis) {
        this.connectionTimeoutMillis = connectionTimeoutMillis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoltClientConfig that = (BoltClientConfig) o;
        return logLeakedSessions == that.logLeakedSessions &&
                encrypted == that.encrypted &&
                maxIdleConnectionPoolSize == that.maxIdleConnectionPoolSize &&
                idleTimeBeforeConnectionTest == that.idleTimeBeforeConnectionTest &&
                routingFailureLimit == that.routingFailureLimit &&
                routingRetryDelayMillis == that.routingRetryDelayMillis &&
                connectionTimeoutMillis == that.connectionTimeoutMillis &&
                Objects.equals(uri, that.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri,
                logLeakedSessions,
                encrypted,
                maxIdleConnectionPoolSize,
                idleTimeBeforeConnectionTest,
                routingFailureLimit,
                routingRetryDelayMillis,
                connectionTimeoutMillis);
    }
}
