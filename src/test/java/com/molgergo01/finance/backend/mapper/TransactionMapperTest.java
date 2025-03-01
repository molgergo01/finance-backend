package com.molgergo01.finance.backend.mapper;

import com.molgergo01.finance.backend.model.entity.Transaction;
import com.molgergo01.finance.backend.model.notification.TransactionNotification;
import org.junit.jupiter.api.Test;

import static com.molgergo01.finance.backend.__utils.TestConstants.TRANSACTION_NOTIFICATION;
import static com.molgergo01.finance.backend.__utils.TestConstants.UUID_1;
import static com.molgergo01.finance.backend.__utils.TestConstants.UUID_2;
import static com.molgergo01.finance.backend.__utils.TestConstants.UUID_3;
import static org.assertj.core.api.Assertions.assertThat;

class TransactionMapperTest {
    @Test
    void shouldMapToTransactionNotification() {
        final Transaction transaction = new Transaction();
        transaction.setId(UUID_3);
        transaction.setSenderId(UUID_1);
        transaction.setRecipientId(UUID_2);
        transaction.setAmount(3000L);

        final TransactionNotification actual = TransactionMapper.mapToTransactionNotification(transaction, "msg");

        assertThat(actual).isEqualTo(TRANSACTION_NOTIFICATION);
    }
}