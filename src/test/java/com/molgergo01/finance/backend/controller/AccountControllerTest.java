package com.molgergo01.finance.backend.controller;

import com.molgergo01.finance.backend.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class AccountControllerTest {
    @MockitoBean
    private AccountService accountServiceMock;

    @Autowired
    private AccountController objectUnderTest;

    @Test
    void shouldReturnResponseEntityWithBalanceAnd200Ok_whenGetBalanceIsCalled() {
        final Long expectedBalance = 3000L;

        doReturn(expectedBalance).when(accountServiceMock).findBalanceById(any());

        final ResponseEntity<Long> response = objectUnderTest.getBalance(UUID.randomUUID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedBalance);
    }
}