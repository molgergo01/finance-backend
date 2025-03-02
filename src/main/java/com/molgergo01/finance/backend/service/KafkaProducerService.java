package com.molgergo01.finance.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.molgergo01.finance.backend.config.kafka.KafkaProperties;
import com.molgergo01.finance.backend.exception.TransactionProcessingException;
import com.molgergo01.finance.backend.model.notification.TransactionNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    public void sendTransactionNotification(final UUID recipientId, final TransactionNotification message) {
        final ObjectMapper objectMapper = new ObjectMapper();
        final String messageJson;

        try {
            messageJson = objectMapper.writeValueAsString(message);
        } catch (Exception ex) {
            log.warn("Failed to serialize Kafka message");
            throw new TransactionProcessingException("Failed to process transaction");
        }

        final ProducerRecord<String, String> record = new ProducerRecord<>(kafkaProperties.getTransactionTopic().getName(),
                                                                           recipientId.toString(),
                                                                           messageJson);
        record.headers().add("recipient_id", recipientId.toString().getBytes(StandardCharsets.UTF_8));

        kafkaTemplate.send(record);
        log.debug("Successfully sent Kafka message to topic: {}", kafkaProperties.getTransactionTopic().getName());
    }
}
