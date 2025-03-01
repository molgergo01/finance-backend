package com.molgergo01.finance.backend.controller;

import com.molgergo01.finance.backend.model.dto.response.SuccessResponse;
import com.molgergo01.finance.backend.model.entity.Transaction;
import com.molgergo01.finance.backend.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Objects;

import static com.molgergo01.finance.backend.__utils.TestConstants.UUID_1;
import static com.molgergo01.finance.backend.__utils.TestConstants.UUID_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = TransactionController.class)
class TransactionControllerTest {
    @MockitoBean
    private TransactionService transactionServiceMock;

    @Autowired
    private TransactionController transactionController;

    @Test
    void shouldReturn201WithCorrectLocationAndSuccessResponse_whenTransactionWasMade() {
        final Transaction transaction = new Transaction();
        transaction.setSenderId(UUID_1);
        transaction.setRecipientId(UUID_2);
        transaction.setAmount(3000L);

        final ResponseEntity<SuccessResponse> response = transactionController.makeTransaction(transaction);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).getMessage()).isEqualTo("Transaction was successful");
        assertThat(response.getBody().getTimestamp()).isNotNull();

        verify(transactionServiceMock).makeTransaction(transaction);
    }
}