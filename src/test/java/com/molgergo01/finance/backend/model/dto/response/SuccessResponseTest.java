package com.molgergo01.finance.backend.model.dto.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SuccessResponseTest {
    @SneakyThrows
    @Test
    void shouldSerialize() {
        final SuccessResponse successResponse = SuccessResponse.builder()
                .timestamp(LocalDateTime.of(2025, 1, 1, 1, 0, 0)) // 2025-01-01 01:00:00
                .message("message")
                .build();

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        final String actual = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(successResponse);

        final File jsonFile = ResourceUtils.getFile("classpath:success_response.json");
        final String expected = Files.readString(jsonFile.toPath());

        assertThat(actual).isEqualTo(expected);
    }
}