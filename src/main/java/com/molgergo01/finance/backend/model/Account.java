package com.molgergo01.finance.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(schema = "finance", name = "accounts")
public class Account {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private Long balance;
}
