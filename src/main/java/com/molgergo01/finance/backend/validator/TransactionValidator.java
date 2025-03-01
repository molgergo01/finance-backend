package com.molgergo01.finance.backend.validator;

import com.molgergo01.finance.backend.model.entity.Transaction;
import org.springframework.stereotype.Service;

@Service
public class TransactionValidator {
    public void validateOnMakeTransaction(final Transaction transaction) {
        if (transaction.getId() != null) {
           throw new IllegalArgumentException("Must not set field: 'id'");
        }
        if (transaction.getTimestamp() != null) {
            throw new IllegalArgumentException("Must not set field: 'timestamp'");
        }
        if (transaction.getSenderId() == null) {
            throw new IllegalArgumentException("Must set field: 'sender_id'");
        }
        if (transaction.getRecipientId() == null) {
            throw new IllegalArgumentException("Must set field: 'recipient_id'");
        }
        if (transaction.getAmount() == null) {
            throw new IllegalArgumentException("Must set field: 'amount'");
        }
        if (transaction.getAmount() <= 0L) {
            throw new IllegalArgumentException("'amount' must be a positive number");
        }
    }
}
