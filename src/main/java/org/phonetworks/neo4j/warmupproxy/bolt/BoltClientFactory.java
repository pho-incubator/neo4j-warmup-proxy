package org.phonetworks.neo4j.warmupproxy.bolt;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class BoltClientFactory extends BaseKeyedPooledObjectFactory<BoltClientConfig, BoltClient> {

    @Override
    public BoltClient create(@NonNull BoltClientConfig clientConfig) {
        return new Neo4jClient();
    }

    @Override
    public PooledObject<BoltClient> wrap(@NonNull BoltClient value) {
        return new DefaultPooledObject<>(value);
    }

    @Override
    public void activateObject(@NonNull BoltClientConfig key, @NonNull PooledObject<BoltClient> p) throws Exception {
        super.activateObject(key, p);

        p.getObject().connect(key);
    }

    @Override
    public void destroyObject(BoltClientConfig key, PooledObject<BoltClient> p) throws Exception {
        p.getObject().disconnect();

        super.destroyObject(key, p);
    }
}
