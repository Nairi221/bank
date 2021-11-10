package com.spring.bank.repository;

import com.spring.bank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionDAO extends JpaRepository<Transaction, Integer> {
    List<Transaction> findAllByTransactionStatus(String transactionStatus);

    Transaction findByid(Integer id);

}
