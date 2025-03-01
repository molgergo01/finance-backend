package com.molgergo01.finance.backend.model.entity;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "transactions")
@Schema(description = "Request body of a transaction")
public class Transaction {
    @Id
    @GeneratedValue
    @Hidden
    private UUID id;

    @Schema(description = "Id of the recipient account", example = "b3965dbc-bdf0-4ee9-9c15-5c205d73fe0a")
    private UUID recipientId;
    @Schema(description = "Id of the sender account", example = "82cbe791-6f72-4d0b-8bb9-78777bdd55e9")
    private UUID senderId;
    @Schema(description = "Amount to be transferred", example = "3000")
    private Long amount;

    @CreatedDate
    @Hidden
    private LocalDateTime timestamp;
}
