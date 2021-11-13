package com.spring.bank.service.impl;

import com.spring.bank.entity.BankAccount;
import com.spring.bank.entity.User;
import com.spring.bank.repository.BankAccountRepo;
import com.spring.bank.repository.UserRepo;
import com.spring.bank.service.BankAccountService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final UserRepo userRepo;
    private final BankAccountRepo bankAccountRepo;

    public BankAccountServiceImpl(UserRepo userRepo, BankAccountRepo bankAccountRepo) {
        this.userRepo = userRepo;
        this.bankAccountRepo = bankAccountRepo;
    }

    @Override
    public BankAccount createBankAccount(Integer loggedUserId, User user) {
        LocalDate date = LocalDate.now();
        User loggedUser = userRepo.findByid(loggedUserId);
        User userToAddBankAcc = userRepo.findByUsername(user.getUsername());
        if (loggedUser.getRole().name().equals("ADMIN")) {
            if (userToAddBankAcc.getBankAccount() == null) {
                BankAccount bankAccount = new BankAccount();
                bankAccount.setBalance(0);
                bankAccount.setCreatedAt(date);
                userToAddBankAcc.setBankAccount(bankAccount);
                bankAccountRepo.save(bankAccount);
                userRepo.save(userToAddBankAcc);
                return bankAccount;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
