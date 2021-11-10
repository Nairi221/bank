package com.spring.bank.controllers;

import com.spring.bank.entity.BankAccount;
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
@RequestMapping("/bank_account")
public class BankAccountController {

  @Autowired UserDAO userDAO;
  @Autowired BankAccountDAO bankAccountDAO;
  @Autowired TransactionDAO transactionDAO;

  
  @PostMapping("/create_bank_account/{id}")
  public ResponseEntity<BankAccount> createBankAccount(@PathVariable Integer id, @RequestBody String userName) {

    LocalDate date = LocalDate.now();
    User user = userDAO.findByid(id);
    User user1 = userDAO.findByUsername(userName);
    if (user.getRole().name().equals("ADMIN")) {
      if (user1.getBankAccount() == null) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(0);
        bankAccount.setCreatedAt(date);
        user.setBankAccount(bankAccount);
        bankAccountDAO.save(bankAccount);
        userDAO.save(user);
        return new ResponseEntity<>(bankAccount, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
      }
    }
    else
    {
      return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }
  }
}
