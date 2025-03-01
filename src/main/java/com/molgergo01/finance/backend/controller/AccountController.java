package com.molgergo01.finance.backend.controller;

import com.molgergo01.finance.backend.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/account")
public class AccountController {
    private final AccountService accountService;

    @GetMapping(value = "/{id}/balance", produces = "application/json")
    public ResponseEntity<Long> getBalance(@PathVariable final UUID id) {
        final Long balance = accountService.findBalanceById(id);

        return ResponseEntity.ok(balance);
    }
}
