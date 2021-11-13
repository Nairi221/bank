package com.spring.bank.service;

import com.spring.bank.entity.BankAccount;
import com.spring.bank.entity.User;

public interface BankAccountService {

    BankAccount createBankAccount(Integer loggedUserId, User user);
}
