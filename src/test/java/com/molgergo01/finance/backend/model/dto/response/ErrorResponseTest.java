package com.molgergo01.finance.backend.model.dto.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {
    @SneakyThrows
    @Test
    void shouldSerialize() {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.of(2025, 1, 1, 1, 0, 0)) // 2025-01-01 01:00:00
                .status(400)
                .error("message")
                .build();

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        final String actual = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorResponse);

        final File jsonFile = ResourceUtils.getFile("classpath:error_response.json");
        final String expected = Files.readString(jsonFile.toPath());

        assertThat(actual).isEqualTo(expected);
    }
}