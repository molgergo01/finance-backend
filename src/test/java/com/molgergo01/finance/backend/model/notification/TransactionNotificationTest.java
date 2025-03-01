package com.molgergo01.finance.backend.model.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;

import static com.molgergo01.finance.backend.__utils.TestConstants.TRANSACTION_NOTIFICATION;
import static org.assertj.core.api.Assertions.assertThat;

class TransactionNotificationTest {
    @SneakyThrows
    @Test
    void shouldSerialize() {

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        final String actual = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(TRANSACTION_NOTIFICATION);

        final File jsonFile = ResourceUtils.getFile("classpath:transaction_notification.json");
        final String expected = Files.readString(jsonFile.toPath());

        assertThat(actual).isEqualTo(expected);
    }
}