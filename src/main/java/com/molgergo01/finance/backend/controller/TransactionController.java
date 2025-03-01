package com.molgergo01.finance.backend.controller;

import com.molgergo01.finance.backend.model.dto.response.ErrorResponse;
import com.molgergo01.finance.backend.model.dto.response.SuccessResponse;
import com.molgergo01.finance.backend.model.entity.Transaction;
import com.molgergo01.finance.backend.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/transaction", produces = "application/json")
@Tag(name = "Transaction API", description = "Operations related to transactions")
public class TransactionController {
    final TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Make a transaction",
               description = "Makes a transaction from the sender account to the recipient's account transferring the requested balance")
    @ApiResponse(responseCode = "201", description = "Transaction was successful",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponse.class)))
    @ApiResponse(responseCode = "404", description = "The sender account or the recipient account does not exist",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "400", description = "Request body is incorrect",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "422", description = "Transaction sender has insufficient balance",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<SuccessResponse> makeTransaction(@RequestBody final Transaction transaction) {
        transactionService.makeTransaction(transaction);

        final SuccessResponse responseBody = SuccessResponse.builder()
                                                            .timestamp(LocalDateTime.now())
                                                            .message("Transaction was successful")
                                                            .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }
}
