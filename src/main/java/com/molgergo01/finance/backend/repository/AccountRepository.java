package com.molgergo01.finance.backend.repository;

import com.molgergo01.finance.backend.model.entity.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
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
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.id = :id")
    void addBalanceById(final UUID id, final Long amount);

    @Modifying
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("UPDATE Account a SET a.balance = a.balance - :amount WHERE a.id = :id")
    void subtractBalanceById(final UUID id, final Long amount);
}
