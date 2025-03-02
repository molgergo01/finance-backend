package com.molgergo01.finance.backend.repository;

import com.molgergo01.finance.backend.model.entity.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends CrudRepository<Account, UUID> {
    Account findAccountById(UUID id);

    @Query("SELECT a.balance FROM Account a WHERE a.id = :id")
    Long findBalanceById(final UUID id);

    @Modifying
    @Query(value = "UPDATE accounts SET balance = balance + :amount WHERE id = :id", nativeQuery = true)
    void addBalanceById(final UUID id, final Long amount);

    @Modifying
    @Query(value = "UPDATE accounts SET balance = balance - :amount WHERE id = :id AND balance >= :amount", nativeQuery = true)
    void subtractBalanceById(final UUID id, final Long amount);
}
