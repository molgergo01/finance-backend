package com.molgergo01.finance.backend.config.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {
    private String bootstrapServer;
    private TransactionTopicProperties transactionTopic;
    private ProducerProperties producer;

    @Data
    public static class TransactionTopicProperties {
        private String name;
        private Integer partitions;
        private Integer replicationFactor;
    }

    @Data
    public static class ProducerProperties {
        final Boolean enableIdempotence;
        final String acks;
        final Integer retries;
    }
}
