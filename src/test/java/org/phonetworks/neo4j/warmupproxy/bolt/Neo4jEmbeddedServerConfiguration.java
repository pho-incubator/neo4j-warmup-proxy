package org.phonetworks.neo4j.warmupproxy.bolt;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.configuration.BoltConnector;
import org.neo4j.kernel.configuration.Settings;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@TestConfiguration
public class Neo4jEmbeddedServerConfiguration {
    public static final URI TEST = URI.create("bolt://neo4j:neo4j@localhost:7777");

    @Bean
    public GraphDatabaseService startServer() throws Exception {
        GraphDatabaseFactory factory = new GraphDatabaseFactory();
        BoltConnector bolt = new BoltConnector("0");
        Path path = Paths.get(System.getProperty("java.io.tmpdir"), "warmup_proxy.db");

        if (!Files.exists(path)) {
            Files.createDirectory(path).toFile();
        }

        return factory.newEmbeddedDatabaseBuilder(path.toFile())
                .setConfig(bolt.enabled, Settings.TRUE)
                .setConfig(bolt.type, "BOLT")
                .setConfig(bolt.listen_address, "localhost:" + TEST.getPort())
                .setConfig(bolt.encryption_level, BoltConnector.EncryptionLevel.OPTIONAL.toString())
                .newGraphDatabase();
    }
}
