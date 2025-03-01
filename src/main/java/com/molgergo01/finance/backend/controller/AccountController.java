package com.molgergo01.finance.backend.controller;

import com.molgergo01.finance.backend.model.dto.response.AccountCreationResponse;
import com.molgergo01.finance.backend.model.dto.response.BalanceResponse;
import com.molgergo01.finance.backend.model.dto.response.ErrorResponse;
import com.molgergo01.finance.backend.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/account")
@Tag(name = "Account API", description = "Operations related to accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping(produces = "application/json")
    @Operation(summary="Create a new account", description = "Creates a new account and return the ID in the body message and in the location header")
    @ApiResponse(responseCode = "201", description = "Successfully created account",
                 headers = @Header(name = "Location", description = "URI of the created account"),
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountCreationResponse.class)))
    public ResponseEntity<AccountCreationResponse> createAccount() {
        final UUID id = accountService.createAccount();

        final AccountCreationResponse responseBody = AccountCreationResponse.builder()
                                                                            .timestamp(LocalDateTime.now())
                                                                            .id(id)
                                                                            .build();

        final String location = String.format("/api/v1/account/%s", id);
        return ResponseEntity.created(URI.create(location)).body(responseBody);
    }

    @GetMapping(value = "/{id}/balance", produces = "application/json")
    @Operation(summary = "Retrieve balance", description = "Retrieves the balance of the account with the provided id")
    @ApiResponse(responseCode = "200", description = "Balance successfully retrieved",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = BalanceResponse.class)))
    @ApiResponse(responseCode = "404", description = "Account not found",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable final UUID id) {
        final Long balance = accountService.findBalanceById(id);

        final BalanceResponse response = BalanceResponse.builder()
                                                        .timestamp(LocalDateTime.now())
                                                        .balance(balance)
                                                        .build();

        return ResponseEntity.ok(response);
    }
}
