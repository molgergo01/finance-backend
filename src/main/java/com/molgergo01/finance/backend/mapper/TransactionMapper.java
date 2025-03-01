package com.molgergo01.finance.backend.mapper;

import com.molgergo01.finance.backend.model.entity.Transaction;
import com.molgergo01.finance.backend.model.notification.TransactionNotification;

public final class TransactionMapper {
    public static TransactionNotification mapToTransactionNotification(final Transaction transaction, final String message) {
        return TransactionNotification.builder()
                .transactionId(transaction.getId())
                .senderId(transaction.getSenderId())
                .recipientId(transaction.getRecipientId())
                .amount(transaction.getAmount())
                .message(message)
                .build();
    }
}
