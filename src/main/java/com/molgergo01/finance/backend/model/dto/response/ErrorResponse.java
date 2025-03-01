package com.molgergo01.finance.backend.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@Schema(description = "Error response")
public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Timestamp of the error", example = "2025-03-01 20:00:00")
    private LocalDateTime timestamp;
    @Schema(description = "Status code of the error", example = "400")
    private int status;
    @Schema(description = "Error message", example = "Account by id 'f2046ca9-2de5-4da2-92e9-e123ff2f02f4' does not exist")
    private String error;
}
