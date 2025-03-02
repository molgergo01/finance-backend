package com.molgergo01.finance.backend.service;

import com.molgergo01.finance.backend.exception.AccountNotFoundException;
import com.molgergo01.finance.backend.exception.InsufficientFundsException;
import com.molgergo01.finance.backend.model.entity.Account;
import com.molgergo01.finance.backend.repository.AccountRepository;
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

@SpringBootTest(classes = AccountService.class)
class AccountServiceTest {
    @MockitoBean
    private AccountRepository accountRepositoryMock;

    @Autowired
    private AccountService objectUnderTest;

    @Test
    void shouldCreateAccount() {
        objectUnderTest.createAccount();

        verify(accountRepositoryMock).save(any(Account.class));
    }

    @Test
    void shouldReturnBalance_whenAccountExists() {
        final Long expected = 3000L;

        doReturn(new Account()).when(accountRepositoryMock).findAccountById(UUID_1);
        doReturn(expected).when(accountRepositoryMock).findBalanceById(UUID_1);

        final Long actual = objectUnderTest.findBalanceById(UUID_1);

        assertThat(actual).isEqualTo(expected);

        verify(accountRepositoryMock).findAccountById(UUID_1);
        verify(accountRepositoryMock).findBalanceById(UUID_1);
    }

    @Test
    void shouldThrowException_onFindBalanceById_whenAccountDoesNotExist() {
        final String expectedMessage = String.format("Account by id '%s' does not exist", UUID_1);

        doReturn(null).when(accountRepositoryMock).findAccountById(UUID_1);

        assertThatThrownBy(() -> objectUnderTest.findBalanceById(UUID_1))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessage(expectedMessage);

        verify(accountRepositoryMock).findAccountById(UUID_1);
        verify(accountRepositoryMock, never()).findBalanceById(any());
    }

    @Test
    void shouldAddBalance_whenAccountExists() {
        final Long expectedAmount = 3000L;

        final Account account = new Account();

        doReturn(account).when(accountRepositoryMock).findAccountById(UUID_1);

        objectUnderTest.addBalance(UUID_1, expectedAmount);

        verify(accountRepositoryMock).findAccountById(UUID_1);
        verify(accountRepositoryMock).save(account);
    }

    @Test
    void shouldThrowException_onAddBalance_whenAccountDoesNotExist() {
        final String expectedMessage = String.format("Account by id '%s' does not exist", UUID_1);

        doReturn(null).when(accountRepositoryMock).findAccountById(UUID_1);

        assertThatThrownBy(() -> objectUnderTest.addBalance(UUID_1, 3000L))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessage(expectedMessage);

        verify(accountRepositoryMock).findAccountById(UUID_1);
        verify(accountRepositoryMock, never()).save(any());
    }

    @Test
    void shouldSubtractBalance_whenSufficientBalanceIsPresentAndAccountExists() {
        final Long expectedAmount = 3000L;

        final Account account = new Account();
        account.setBalance(expectedAmount);

        doReturn(account).when(accountRepositoryMock).findAccountById(UUID_1);

        objectUnderTest.subtractBalance(UUID_1, expectedAmount);

        verify(accountRepositoryMock).findAccountById(UUID_1);
        verify(accountRepositoryMock).save(account);
    }

    @Test
    void shouldThrowException_onSubtractBalance_whenAccountDoesNotExist() {
        final String expectedMessage = String.format("Account by id '%s' does not exist", UUID_1);

        doReturn(null).when(accountRepositoryMock).findAccountById(UUID_1);

        assertThatThrownBy(() -> objectUnderTest.subtractBalance(UUID_1, 3000L))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessage(expectedMessage);

        verify(accountRepositoryMock).findAccountById(UUID_1);
        verify(accountRepositoryMock, never()).findBalanceById(any());
        verify(accountRepositoryMock, never()).save(any());
    }

    @Test
    void shouldThrowException_onSubtractBalance_whenInsufficientBalanceIsPresent() {
        final String expectedMessage = String.format("Insufficient funds for account: '%s'", UUID_1);

        doReturn(new Account()).when(accountRepositoryMock).findAccountById(UUID_1);
        doReturn(2000L).when(accountRepositoryMock).findBalanceById(UUID_1);

        assertThatThrownBy(() -> objectUnderTest.subtractBalance(UUID_1, 3000L))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessage(expectedMessage);

        verify(accountRepositoryMock).findAccountById(UUID_1);
        verify(accountRepositoryMock, never()).save(any());
    }
}