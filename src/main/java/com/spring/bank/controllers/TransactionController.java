package com.spring.bank.controllers;


import com.spring.bank.entity.Transaction;
import com.spring.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/make_transaction/{id}")
    public ResponseEntity<String> makeTransactionPost(
            @PathVariable Integer id, @RequestBody Transaction transaction) {

        Boolean hasCreatedTransaction = transactionService.createTransaction(id, transaction);
        if (hasCreatedTransaction) {
            return new ResponseEntity<>("transaction created with pending status", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("a problem occurred", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * @param id          .
     * @param transaction .
     * @return .
     */
    @DeleteMapping("/cancel_transaction/{id}")
    public ResponseEntity<String> cancelTransaction(
            @PathVariable Integer id, @RequestBody Transaction transaction) {

        Boolean hasCanceledTransaction = transactionService.cancelTransaction(id, transaction);
        if (hasCanceledTransaction) {
            return new ResponseEntity<>("transaction canceled, success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("problem occurred", HttpStatus.NOT_ACCEPTABLE);
        }

    }

    /**
     * @param id .
     * @return .
     */
    @GetMapping("/accept_transactions/{id}")
    public ResponseEntity<List<Transaction>> acceptTransactionsGet(@PathVariable Integer id) {
        List<Transaction> transactions = transactionService.getAllPendingTransactions(id);
        if (transactions != null){
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * @param id          .
     * @param transaction .
     * @return .
     */
    @PostMapping("/accept_transactions/{id}")
    public ResponseEntity<String> acceptTransactionsPost(
            @PathVariable Integer id, @RequestBody Transaction transaction) {

        Boolean hasAcceptedTransaction = transactionService.acceptTransaction(id, transaction);
        if (hasAcceptedTransaction){
            return new ResponseEntity<>("approved", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
