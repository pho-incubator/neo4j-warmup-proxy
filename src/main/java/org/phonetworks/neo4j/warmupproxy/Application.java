package org.phonetworks.neo4j.warmupproxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.phonetworks.neo4j.warmupproxy.config.ServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.integration.ip.tcp.connection.AbstractConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.integration.transformer.ObjectToStringTransformer;

import org.springframework.messaging.MessageChannel;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Application {
    private static final String INPUT_CHANNEL_NAME = "inputChannel";
    private static final String SERVER_CHANNEL_NAME = "serverChannel";
    private static final String OUTPUT_CHANNEL_NAME = "inputChannel";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private ServerConfiguration appConfig;

    @Autowired
    private Aggregator aggregator;

    @Bean
    public AbstractConnectionFactory getConnectionFactory() {
        TcpNetServerConnectionFactory factory = new TcpNetServerConnectionFactory(appConfig.getSocketPort());
        factory.setSoKeepAlive(appConfig.isSocketKeepAlive());
        factory.setSoTimeout(appConfig.getSocketTimeoutMillis());
        return factory;
    }

    @Bean(name = INPUT_CHANNEL_NAME)
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean(name = OUTPUT_CHANNEL_NAME)
    public MessageChannel outputChannel() {
        return new DirectChannel();
    }

    @Bean
    public TcpReceivingChannelAdapter inbound(AbstractConnectionFactory connectionFactory) {
        TcpReceivingChannelAdapter adapter = new TcpReceivingChannelAdapter();
        adapter.setConnectionFactory(connectionFactory);
        adapter.setOutputChannel(inputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = OUTPUT_CHANNEL_NAME)
    public TcpSendingMessageHandler outbound(AbstractConnectionFactory connectionFactory) {
        TcpSendingMessageHandler handler = new TcpSendingMessageHandler();
        handler.setConnectionFactory(connectionFactory);
        return handler;
    }

    @Bean
    @Transformer(inputChannel = INPUT_CHANNEL_NAME, outputChannel = SERVER_CHANNEL_NAME)
    public ObjectToStringTransformer inputTransformer() {
        return new ObjectToStringTransformer();
    }

    @ServiceActivator(inputChannel = SERVER_CHANNEL_NAME, outputChannel = OUTPUT_CHANNEL_NAME)
    public String service(String cypherQuery) {
        try {
            List<Map<String, Object>> result = aggregator.run(cypherQuery);

            // TODO revisit serialization approach
            return new ObjectMapper().writeValueAsString(result);
        } catch (Exception e) {
            return "{ \"error\": \"" + e.getMessage() + "\" }";
        }
    }
}
