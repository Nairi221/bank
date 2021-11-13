package com.spring.bank.controllers;

import com.spring.bank.entity.BankAccount;
import com.spring.bank.entity.User;
import com.spring.bank.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/bank_account")
public class BankAccountController {

  @Autowired
  BankAccountService bankAccountService;

  @PostMapping("/create_bank_account/{id}")
  public ResponseEntity<BankAccount> createBankAccount(@PathVariable Integer id, @RequestBody User user) {
     BankAccount bankAccount = bankAccountService.createBankAccount(id, user);
     if (bankAccount != null){
       return new ResponseEntity<>(bankAccount, HttpStatus.OK);
     }
     else {
       return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
     }
  }
}
