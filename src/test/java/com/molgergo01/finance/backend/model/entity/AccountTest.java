package com.molgergo01.finance.backend.model.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {
    @Test
    void shouldReturnAccountWithZeroBalance_whenDefaultConstructorIsCalled() {
        final Account account = new Account();
        assertThat(account.getId()).isNull();
        assertThat(account.getBalance()).isZero();
    }
}