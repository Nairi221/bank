package com.spring.bank.service;

import com.spring.bank.entity.Transaction;

import java.util.List;

public interface TransactionService {

    Boolean createTransaction(Integer loggedId, Transaction transaction);
    Boolean cancelTransaction(Integer loggedId, Transaction transaction);
    List<Transaction> getAllPendingTransactions(Integer loggedId);
    Boolean acceptTransaction(Integer loggedId, Transaction transaction);

}
