package com.molgergo01.finance.backend.service;

import com.molgergo01.finance.backend.mapper.TransactionMapper;
import com.molgergo01.finance.backend.model.entity.Transaction;
import com.molgergo01.finance.backend.model.notification.TransactionNotification;
import com.molgergo01.finance.backend.repository.TransactionRepository;
import com.molgergo01.finance.backend.validator.TransactionValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {
    final TransactionValidator transactionValidator;
    final TransactionRepository transactionRepository;
    final AccountService accountService;
    final KafkaProducerService kafkaProducerService;

    public void makeTransaction(final Transaction transaction) {
        transactionValidator.validateOnMakeTransaction(transaction);

        transactionRepository.save(transaction);

        accountService.subtractBalance(transaction.getSenderId(), transaction.getAmount());
        accountService.addBalance(transaction.getRecipientId(), transaction.getAmount());

        final TransactionNotification transactionNotification =
                TransactionMapper.mapToTransactionNotification(transaction, "Transaction received");

        kafkaProducerService.sendTransactionNotification(transactionNotification.getRecipientId(), transactionNotification);

    }
}
