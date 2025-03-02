package com.molgergo01.finance.backend.integration;

import com.molgergo01.finance.backend.model.dto.response.AccountCreationResponse;
import com.molgergo01.finance.backend.model.dto.response.BalanceResponse;
import com.molgergo01.finance.backend.model.dto.response.ErrorResponse;
import com.molgergo01.finance.backend.model.dto.response.SuccessResponse;
import com.molgergo01.finance.backend.model.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import static com.molgergo01.finance.backend.__utils.TestConstants.UUID_1;
import static com.molgergo01.finance.backend.__utils.TestConstants.UUID_2;
import static com.molgergo01.finance.backend.__utils.TestConstants.UUID_3;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FinanceApiIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");
    @Container
    static KafkaContainer kafka = new KafkaContainer("apache/kafka-native:3.8.0");

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TestRestTemplate restTemplate;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("kafka.bootstrap-server", kafka::getBootstrapServers);
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE accounts RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("INSERT INTO accounts VALUES ('b3965dbc-bdf0-4ee9-9c15-5c205d73fe0a', 3000)");
        jdbcTemplate.execute("INSERT INTO accounts VALUES ('82cbe791-6f72-4d0b-8bb9-78777bdd55e9', 0)");

        baseUrl=String.format("http://localhost:%s", port);
    }

    @Test
    void shouldCreateAccount() {
        final String url = String.format("%s/api/v1/account", baseUrl);
        final ResponseEntity<AccountCreationResponse> response =
                restTemplate.postForEntity(url, null, AccountCreationResponse.class);

        assertThat(Objects.requireNonNull(response.getHeaders().get("Location")).getFirst()).contains("/api/v1/account/");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).getId()).isNotNull();

        final List<Long> balances = jdbcTemplate.queryForList("SELECT balance FROM accounts", Long.class);
        assertThat(balances).hasSize(3);
        assertThat(balances.get(2)).isEqualTo(0L);
    }

    @Test
    void shouldReturnAccountBalance() {
        final String url = String.format("%s/api/v1/account/%s/balance", baseUrl, UUID_1);
        final ResponseEntity<BalanceResponse> response = restTemplate.getForEntity(url, BalanceResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getBalance()).isEqualTo(3000L);
    }

    @Test
    void shouldReturnErrorWith404_onAccountGetBalance_ifAccountDoesNotExist() {
        final String expectedError = String.format("Account by id '%s' does not exist", UUID_3);

        final String url = String.format("%s/api/v1/account/%s/balance", baseUrl, UUID_3);
        final ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(url, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(Objects.requireNonNull(response.getBody()).getStatus()).isEqualTo(404);
        assertThat(Objects.requireNonNull(response.getBody()).getError()).isEqualTo(expectedError);
    }

    @Test
    void shouldMakeTransaction() {
        final Transaction transaction = new Transaction();
        transaction.setSenderId(UUID_1);
        transaction.setRecipientId(UUID_2);
        transaction.setAmount(1000L);

        final String url = String.format("%s/api/v1/transaction", baseUrl);
        final ResponseEntity<SuccessResponse> response = restTemplate.postForEntity(url, transaction, SuccessResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).getMessage()).isEqualTo("Transaction was successful");

        final String sql1 = String.format("SELECT balance FROM accounts WHERE id = '%s'", UUID_1);
        final String sql2 = String.format("SELECT balance FROM accounts WHERE id = '%s'", UUID_2);

        final Long uuid1Balance = jdbcTemplate.queryForObject(sql1, Long.class);
        assertThat(uuid1Balance).isEqualTo(2000L);

        final Long uuid2Balance = jdbcTemplate.queryForObject(sql2, Long.class);
        assertThat(uuid2Balance).isEqualTo(1000L);

        final Transaction transactionRow = jdbcTemplate.query("SELECT id, sender_id, recipient_id, amount, timestamp FROM transactions", transactionRowMapper).getFirst();
        assertThat(transactionRow.getId()).isNotNull();
        assertThat(transactionRow.getSenderId()).isEqualTo(UUID_1);
        assertThat(transactionRow.getRecipientId()).isEqualTo(UUID_2);
        assertThat(transactionRow.getAmount()).isEqualTo(1000L);
        assertThat(transactionRow.getTimestamp()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.MINUTES));
    }

    @Test
    void shouldReturnErrorWith404_onMakeTransaction_whenSenderDoesNotExist() {
        final String expectedError = String.format("Account by id '%s' does not exist", UUID_3);

        final Transaction transaction = new Transaction();
        transaction.setSenderId(UUID_3);
        transaction.setRecipientId(UUID_2);
        transaction.setAmount(1000L);

        final String url = String.format("%s/api/v1/transaction", baseUrl);
        final ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(url, transaction, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(Objects.requireNonNull(response.getBody()).getStatus()).isEqualTo(404);
        assertThat(Objects.requireNonNull(response.getBody()).getError()).isEqualTo(expectedError);
    }

    @Test
    void shouldReturnErrorWith404_onMakeTransaction_whenRecipientDoesNotExist() {
        final String expectedError = String.format("Account by id '%s' does not exist", UUID_3);

        final Transaction transaction = new Transaction();
        transaction.setSenderId(UUID_1);
        transaction.setRecipientId(UUID_3);
        transaction.setAmount(1000L);

        final String url = String.format("%s/api/v1/transaction", baseUrl);
        final ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(url, transaction, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(Objects.requireNonNull(response.getBody()).getStatus()).isEqualTo(404);
        assertThat(Objects.requireNonNull(response.getBody()).getError()).isEqualTo(expectedError);
    }

    @Test
    void shouldReturnErrorWith422_onMakeTransaction_whenSenderHasInsufficientFunds() {
        final String expectedError = String.format("Insufficient funds for account: '%s'", UUID_2);

        final Transaction transaction = new Transaction();
        transaction.setSenderId(UUID_2);
        transaction.setRecipientId(UUID_1);
        transaction.setAmount(1000L);

        final String url = String.format("%s/api/v1/transaction", baseUrl);
        final ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(url, transaction, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(Objects.requireNonNull(response.getBody()).getStatus()).isEqualTo(422);
        assertThat(Objects.requireNonNull(response.getBody()).getError()).isEqualTo(expectedError);
    }

    @ParameterizedTest
    @MethodSource("provideForInvalidTransaction")
    void shouldReturnErrorWith400_onMakeTransaction_ifValidationFailsOnTransaction(final UUID id,
                                                                                   final UUID senderId,
                                                                                   final UUID recipientId,
                                                                                   final Long amount,
                                                                                   final LocalDateTime timestamp,
                                                                                   final String errorMessage) {
        final Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setSenderId(senderId);
        transaction.setRecipientId(recipientId);
        transaction.setAmount(amount);
        transaction.setTimestamp(timestamp);

        final String url = String.format("%s/api/v1/transaction", baseUrl);
        final ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(url, transaction, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(Objects.requireNonNull(response.getBody()).getStatus()).isEqualTo(400);
        assertThat(Objects.requireNonNull(response.getBody()).getError()).isEqualTo(errorMessage);
    }

    private static Stream<Arguments> provideForInvalidTransaction() {
        return Stream.of(
                Arguments.of(UUID_1, UUID_1, UUID_2, 3000L, null, "Must not set field: 'id'"),
                Arguments.of(null, UUID_1, UUID_2, 3000L, LocalDateTime.now(), "Must not set field: 'timestamp'"),
                Arguments.of(null, null, UUID_2, 3000L, null, "Must set field: 'sender_id'"),
                Arguments.of(null, UUID_1, null, 3000L, null, "Must set field: 'recipient_id'"),
                Arguments.of(null, UUID_1, UUID_2, null, null, "Must set field: 'amount'"),
                Arguments.of(null, UUID_1, UUID_2, 0L, null, "'amount' must be a positive number"),
                Arguments.of(null, UUID_1, UUID_2, -3000L, null, "'amount' must be a positive number")
        );
    }

    private final RowMapper<Transaction> transactionRowMapper = (rs, rowNum) -> {
        Transaction transaction = new Transaction();
        transaction.setId(rs.getObject("id", UUID.class));
        transaction.setSenderId(rs.getObject("sender_id", UUID.class));
        transaction.setRecipientId(rs.getObject("recipient_id", UUID.class));
        transaction.setAmount(rs.getLong("amount"));
        transaction.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
        return transaction;
    };
}
