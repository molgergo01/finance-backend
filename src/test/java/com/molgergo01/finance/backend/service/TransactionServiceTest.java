package com.molgergo01.finance.backend.service;

import com.molgergo01.finance.backend.model.entity.Transaction;
import com.molgergo01.finance.backend.repository.TransactionRepository;
import com.molgergo01.finance.backend.validator.TransactionValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static com.molgergo01.finance.backend.__utils.TestConstants.UUID_1;
import static com.molgergo01.finance.backend.__utils.TestConstants.UUID_2;
import static org.mockito.Mockito.verify;

@SpringBootTest
class TransactionServiceTest {
    @MockitoBean
    private TransactionValidator transactionValidatorMock;
    @MockitoBean
    private TransactionRepository transactionRepositoryMock;
    @MockitoBean
    private AccountService accountServiceMock;

    @Autowired
    private TransactionService objectUnderTest;

    @Test
    void shouldSubtractBalanceFromSenderAndAddBalanceToRecipientAndSaveTransaction_whenTransactionIsMade() {
        final Long expectedAmount = 3000L;

        final Transaction transaction = new Transaction();
        transaction.setSenderId(UUID_1);
        transaction.setRecipientId(UUID_2);
        transaction.setAmount(expectedAmount);

        objectUnderTest.makeTransaction(transaction);

        verify(transactionValidatorMock).validateOnMakeTransaction(transaction);
        verify(transactionRepositoryMock).save(transaction);
        verify(accountServiceMock).subtractBalance(UUID_1, expectedAmount);
        verify(accountServiceMock).addBalance(UUID_2, expectedAmount);
    }
}