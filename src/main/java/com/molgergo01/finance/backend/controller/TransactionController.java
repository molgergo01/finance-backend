package com.molgergo01.finance.backend.controller;

import com.molgergo01.finance.backend.model.dto.response.SuccessResponse;
import com.molgergo01.finance.backend.model.entity.Transaction;
import com.molgergo01.finance.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/transaction", produces = "application/json")
public class TransactionController {
    final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<SuccessResponse> makeTransaction(@RequestBody final Transaction transaction) {
        transactionService.makeTransaction(transaction);

        final SuccessResponse responseBody = SuccessResponse.builder()
                                                            .timestamp(LocalDateTime.now())
                                                            .message("Transaction was successful")
                                                            .build();

        return ResponseEntity.ok(responseBody);
    }
}
