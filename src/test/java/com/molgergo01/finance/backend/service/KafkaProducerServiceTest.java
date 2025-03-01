package com.molgergo01.finance.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.nio.charset.StandardCharsets;

import static com.molgergo01.finance.backend.__utils.TestConstants.TRANSACTION_NOTIFICATION;
import static com.molgergo01.finance.backend.__utils.TestConstants.UUID_2;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("kafka-test")
class KafkaProducerServiceTest {
    @MockitoBean
    private KafkaTemplate<String, String> kafkaTemplateMock;

    @Autowired
    private KafkaProducerService objectUnderTest;

    @SneakyThrows
    @Test
    void shouldSendTransactionNotification() {
        final ObjectMapper objectMapper = new ObjectMapper();
        final String messageJson = objectMapper.writeValueAsString(TRANSACTION_NOTIFICATION);

        final ProducerRecord<String, String> expected = new ProducerRecord<>("transaction-json",
                UUID_2.toString(),
                messageJson);
        expected.headers().add("recipient_id", UUID_2.toString().getBytes(StandardCharsets.UTF_8));

        objectUnderTest.sendTransactionNotification(UUID_2, TRANSACTION_NOTIFICATION);

        verify(kafkaTemplateMock).send(expected);
    }
}