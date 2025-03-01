package com.molgergo01.finance.backend.model.entity;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@Table(schema = "finance", name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID recipientId;
    private UUID senderId;
    private Long amount;

    @CreatedDate
    private LocalDateTime timestamp;
}
