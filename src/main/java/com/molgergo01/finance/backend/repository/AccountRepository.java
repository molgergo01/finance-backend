package com.molgergo01.finance.backend.repository;

import com.molgergo01.finance.backend.model.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends CrudRepository<Account, UUID> {
    @Query("SELECT a.balance FROM Account a WHERE a.id = :uuid")
    Long findBalanceById(final UUID uuid);
}
