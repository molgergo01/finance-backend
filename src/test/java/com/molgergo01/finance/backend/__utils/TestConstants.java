package com.molgergo01.finance.backend.__utils;

import com.molgergo01.finance.backend.model.notification.TransactionNotification;

import java.util.UUID;

public class TestConstants {
    public static final UUID UUID_1 = UUID.fromString("b3965dbc-bdf0-4ee9-9c15-5c205d73fe0a");
    public static final UUID UUID_2 = UUID.fromString("82cbe791-6f72-4d0b-8bb9-78777bdd55e9");
    public static final UUID UUID_3 = UUID.fromString("a596738a-1743-4e0a-bec3-4c9764747198");
    public static final TransactionNotification TRANSACTION_NOTIFICATION = TransactionNotification.builder()
                                                                                                  .transactionId(UUID_3)
                                                                                                  .senderId(UUID_1)
                                                                                                  .recipientId(UUID_2)
                                                                                                  .amount(3000L)
                                                                                                  .message("msg")
                                                                                                  .build();
}
