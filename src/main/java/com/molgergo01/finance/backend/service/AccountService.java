package com.molgergo01.finance.backend.service;

import com.molgergo01.finance.backend.exception.InsufficientFundsException;
import com.molgergo01.finance.backend.model.entity.Account;
import com.molgergo01.finance.backend.exception.AccountNotFoundException;
import com.molgergo01.finance.backend.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public UUID createAccount() {
        final Account account = new Account();

        accountRepository.save(account);

        log.debug("Successfully persisted new account");
        return account.getId();
    }

    public Long findBalanceById(final UUID id) {
        final Account account = accountRepository.findAccountById(id);

        if (account == null) {
            throw new AccountNotFoundException(String.format("Account by id '%s' does not exist", id));
        }

        return accountRepository.findBalanceById(id);
    }

    public void addBalance(final UUID id, final Long amount) {
        final Account account = accountRepository.findAccountById(id);

        if (account == null) {
           throw new AccountNotFoundException(String.format("Account by id '%s' does not exist", id));
        }

        account.setBalance(account.getBalance() + amount);

        accountRepository.save(account);
        log.debug("Successfully added to balance");
    }
    public void subtractBalance(final UUID id, final Long amount) {
        final Account account = accountRepository.findAccountById(id);

        if (account == null) {
            throw new AccountNotFoundException(String.format("Account by id '%s' does not exist", id));
        }
        if (account.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds for sender account");
        }

        account.setBalance(account.getBalance() - amount);

        accountRepository.save(account);
        log.debug("Successfully subtracted from balance");
    }
}
