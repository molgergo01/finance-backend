package com.molgergo01.finance.backend.model.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import static com.molgergo01.finance.backend.__utils.TestConstants.UUID_1;
import static com.molgergo01.finance.backend.__utils.TestConstants.UUID_2;
import static org.assertj.core.api.Assertions.assertThat;

class TransactionTest {
    @SneakyThrows
    @Test
    void shouldDeserialize() {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Transaction actual = objectMapper.readValue(ResourceUtils.getFile("classpath:transaction.json"), Transaction.class);

        final Transaction expected = new Transaction();
        expected.setSenderId(UUID_1);
        expected.setRecipientId(UUID_2);
        expected.setAmount(3000L);

        assertThat(actual).isEqualTo(expected);
    }
}