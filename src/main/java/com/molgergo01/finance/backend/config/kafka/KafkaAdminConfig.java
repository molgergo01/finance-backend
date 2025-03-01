package com.molgergo01.finance.backend.config.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class KafkaAdminConfig {
    private final KafkaProperties kafkaProperties;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServer());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic transactionTopic() {
        final KafkaProperties.TransactionTopicProperties properties = kafkaProperties.getTransactionTopic();
        return new NewTopic(properties.getName(),
                            properties.getPartitions(),
                            properties.getReplicationFactor().shortValue());
    }
}
