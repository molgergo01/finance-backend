package com.molgergo01.finance.backend.service;

import com.molgergo01.finance.backend.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class AccountServiceTest {
    @MockitoBean
    private AccountRepository accountRepository;

    @Autowired
    private AccountService objectUnderTest;

    @Test
    void shouldReturnBalance_whenAccountExists() {
        final Long expected = 3000L;

        doReturn(expected).when(accountRepository).findBalanceById(any());

        final Long actual = objectUnderTest.findBalanceById(UUID.randomUUID());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldThrowException_whenAccountDoesNotExist() {
        final UUID uuid = UUID.fromString("a596738a-1743-4e0a-bec3-4c9764747198");
        final String expectedMessage = String.format("Account by id '%s' does not exist", uuid);

        doReturn(null).when(accountRepository).findBalanceById(uuid);

        assertThatThrownBy(() -> objectUnderTest.findBalanceById(uuid))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(expectedMessage);
    }
}