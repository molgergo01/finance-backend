package com.molgergo01.finance.backend.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(schema = "finance", name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID recipientId;
    private UUID senderId;
    private Long amount;

    private LocalDateTime timestamp;

}
