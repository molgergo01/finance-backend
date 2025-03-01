package com.molgergo01.finance.backend.service;

import com.molgergo01.finance.backend.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static com.molgergo01.finance.backend.__utils.TestConstants.UUID_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest
class AccountServiceTest {
    @MockitoBean
    private AccountRepository accountRepositoryMock;

    @Autowired
    private AccountService objectUnderTest;

    @Test
    void shouldReturnBalance_whenAccountExists() {
        final Long expected = 3000L;

        doReturn(expected).when(accountRepositoryMock).findBalanceById(UUID_1);

        final Long actual = objectUnderTest.findBalanceById(UUID_1);

        assertThat(actual).isEqualTo(expected);

        verify(accountRepositoryMock).findBalanceById(UUID_1);
    }

    @Test
    void shouldThrowExceptionOnFindBalanceById_whenAccountDoesNotExist() {
        final String expectedMessage = String.format("Account by id '%s' does not exist", UUID_1);

        doReturn(null).when(accountRepositoryMock).findBalanceById(UUID_1);

        assertThatThrownBy(() -> objectUnderTest.findBalanceById(UUID_1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(expectedMessage);

        verify(accountRepositoryMock).findBalanceById(UUID_1);
    }

    @Test
    void shouldAddBalance() {
        final Long expectedAmount = 3000L;

        objectUnderTest.addBalance(UUID_1, expectedAmount);

        verify(accountRepositoryMock).addBalanceById(UUID_1, expectedAmount);
    }

    @Test
    void shouldSubtractBalance_whenSufficientBalanceIsPresent() {
        final Long expectedAmount = 3000L;

        doReturn(expectedAmount).when(accountRepositoryMock).findBalanceById(UUID_1);

        objectUnderTest.subtractBalance(UUID_1, expectedAmount);

        verify(accountRepositoryMock).findBalanceById(UUID_1);
        verify(accountRepositoryMock).subtractBalanceById(UUID_1, expectedAmount);
    }

    @Test
    void shouldThrowExceptionOnSubtractBalance_whenInsufficientBalanceIsPresent() {
        final String expectedMessage = String.format("Insufficient funds for account: '%s'", UUID_1);

        doReturn(2000L).when(accountRepositoryMock).findBalanceById(UUID_1);

        assertThatThrownBy(() -> objectUnderTest.subtractBalance(UUID_1, 3000L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(expectedMessage);

        verify(accountRepositoryMock).findBalanceById(UUID_1);
        verify(accountRepositoryMock, never()).subtractBalanceById(any(), any());
    }
}