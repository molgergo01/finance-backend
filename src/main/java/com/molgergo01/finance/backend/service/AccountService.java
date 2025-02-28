package com.molgergo01.finance.backend.service;

import com.molgergo01.finance.backend.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Long findBalanceById(final UUID id) {
        final Long balance = accountRepository.findBalanceById(id);

        if (balance == null) {
           throw new EntityNotFoundException(String.format("Account by id '%s' does not exist", id));
        }

        return balance;
    }

    public void addBalance(final UUID id, final Long amount) {
        accountRepository.addBalanceById(id, amount);
    }
    public void subtractBalance(final UUID id, final Long amount) {
        final long balance = findBalanceById(id);
        if (balance < amount) {
            throw new IllegalStateException(String.format("Insufficient funds for account: '%s'", id));
        }

        accountRepository.subtractBalanceById(id, amount);
    }
}
