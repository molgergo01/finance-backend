package com.molgergo01.finance.backend.controller;

import com.molgergo01.finance.backend.model.dto.response.SuccessResponse;
import com.molgergo01.finance.backend.service.AccountService;
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
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<SuccessResponse> createAccount() {
        final UUID id = accountService.createAccount();

        final SuccessResponse successResponse = SuccessResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(String.format("Successfully created account with id: %s", id))
                .build();

        final String location = String.format("/api/v1/account/%s", id);
        return ResponseEntity.created(URI.create(location)).body(successResponse);
    }

    @GetMapping(value = "/{id}/balance", produces = "application/json")
    public ResponseEntity<Long> getBalance(@PathVariable final UUID id) {
        final Long balance = accountService.findBalanceById(id);

        return ResponseEntity.ok(balance);
    }
}
