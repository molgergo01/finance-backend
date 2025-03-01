package com.molgergo01.finance.backend.model.notification;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransactionNotification {
    private UUID transactionId;
    private UUID senderId;
    private UUID recipientId;
    private Long amount;
    private String message;
}
