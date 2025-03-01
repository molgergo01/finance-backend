package com.molgergo01.finance.backend.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@Schema(description = "Successful balance retrieval response")
public class BalanceResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Timestamp of the balance retrieval", example = "2025-03-01 20:00:00")
    private LocalDateTime timestamp;
    @Schema(description = "Balance of the account", example = "3000")
    private Long balance;
}
