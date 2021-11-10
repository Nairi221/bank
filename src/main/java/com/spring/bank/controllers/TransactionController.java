package com.spring.bank.controllers;

import com.spring.bank.entity.Transaction;
import com.spring.bank.entity.User;
import com.spring.bank.repository.BankAccountDAO;
import com.spring.bank.repository.TransactionDAO;
import com.spring.bank.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

  @Autowired UserDAO userDAO;
  @Autowired BankAccountDAO bankAccountDAO;
  @Autowired TransactionDAO transactionDAO;

  @PostMapping("/make_transaction/{id}")
  public ResponseEntity<String> makeTransactionPost(
      @PathVariable Integer id, @RequestBody Transaction transaction) {

    User user = userDAO.findByid(id);
    if (user.getBankAccount() != null
        && transaction.getTransactionSum() != null
        && transaction.getTransactionType() != null
        && (transaction.getTransactionType().equals("deposit")
            || transaction.getTransactionType().equals("withdraw"))) {

      LocalDate date = LocalDate.now();
      transaction.setCreatedAt(date);
      transaction.setUser(user);
      transaction.setTransactionStatus("pending");
      transactionDAO.save(transaction);
      return new ResponseEntity<>("transaction created with pending status", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("a problem occurred", HttpStatus.NOT_ACCEPTABLE);
    }
  }
}
