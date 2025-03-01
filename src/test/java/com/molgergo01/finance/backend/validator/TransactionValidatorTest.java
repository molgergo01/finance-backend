package com.molgergo01.finance.backend.validator;

import com.molgergo01.finance.backend.model.entity.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static com.molgergo01.finance.backend.__utils.TestConstants.UUID_1;
import static com.molgergo01.finance.backend.__utils.TestConstants.UUID_2;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = TransactionValidator.class)
class TransactionValidatorTest {
    @Autowired
    private TransactionValidator objectUnderTest;

    @Test
    void shouldNotThrowException_onValidateOnMakeTransaction_whenTransactionIsValid() {
        final Transaction transaction = new Transaction();
        transaction.setSenderId(UUID_1);
        transaction.setRecipientId(UUID_1);
        transaction.setAmount(3000L);

        assertThatCode(() -> objectUnderTest.validateOnMakeTransaction(transaction)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("provideForInvalidMakeTransaction")
    void shouldThrowException_onValidateOnMakeTransaction_whenTransactionIsInvalid(final UUID id,
                                                                                   final UUID senderId,
                                                                                   final UUID recipientId,
                                                                                   final Long amount,
                                                                                   final LocalDateTime timestamp,
                                                                                   final Exception exception) {
        final Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setSenderId(senderId);
        transaction.setRecipientId(recipientId);
        transaction.setAmount(amount);
        transaction.setTimestamp(timestamp);

        assertThatThrownBy(() -> objectUnderTest.validateOnMakeTransaction(transaction))
                .isInstanceOf(exception.getClass())
                .hasMessage(exception.getMessage());
    }

    private static Stream<Arguments> provideForInvalidMakeTransaction() {
        return Stream.of(
                Arguments.of(UUID_1, UUID_1, UUID_2, 3000L, null, new IllegalArgumentException("Must not set field: 'id'")),
                Arguments.of(null, UUID_1, UUID_2, 3000L, LocalDateTime.now(), new IllegalArgumentException("Must not set field: 'timestamp'")),
                Arguments.of(null, null, UUID_2, 3000L, null, new IllegalArgumentException("Must set field: 'sender_id'")),
                Arguments.of(null, UUID_1, null, 3000L, null, new IllegalArgumentException("Must set field: 'recipient_id'")),
                Arguments.of(null, UUID_1, UUID_2, null, null, new IllegalArgumentException("Must set field: 'amount'")),
                Arguments.of(null, UUID_1, UUID_2, 0L, null, new IllegalArgumentException("'amount' must be a positive number")),
                Arguments.of(null, UUID_1, UUID_2, -3000L, null, new IllegalArgumentException("'amount' must be a positive number"))
        );
    }
}