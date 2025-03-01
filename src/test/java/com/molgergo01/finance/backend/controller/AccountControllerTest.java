package com.molgergo01.finance.backend.controller;

import com.molgergo01.finance.backend.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static com.molgergo01.finance.backend.__utils.TestConstants.UUID_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@SpringBootTest
class AccountControllerTest {
    @MockitoBean
    private AccountService accountServiceMock;

    @Autowired
    private AccountController objectUnderTest;

    @Test
    void shouldReturnResponseEntityWithBalanceAnd200Ok_whenGetBalanceIsCalled() {
        final Long expectedBalance = 3000L;

        doReturn(expectedBalance).when(accountServiceMock).findBalanceById(UUID_1);

        final ResponseEntity<Long> response = objectUnderTest.getBalance(UUID_1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedBalance);

        verify(accountServiceMock).findBalanceById(UUID_1);
    }
}