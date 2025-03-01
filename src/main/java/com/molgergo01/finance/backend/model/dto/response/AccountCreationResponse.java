package com.molgergo01.finance.backend.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@Schema(description = "Successful account creation response")
public class AccountCreationResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Timestamp of the account creation", example = "2025-03-01 20:00:00")
    private LocalDateTime timestamp;
    @Schema(description = "UUID of the created account", example = "f2046ca9-2de5-4da2-92e9-e123ff2f02f4")
    private UUID id;
}
