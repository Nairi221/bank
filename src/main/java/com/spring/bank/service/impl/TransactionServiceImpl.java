package com.spring.bank.service.impl;

import com.spring.bank.entity.BankAccount;
import com.spring.bank.entity.Transaction;
import com.spring.bank.entity.User;
import com.spring.bank.repository.BankAccountRepo;
import com.spring.bank.repository.TransactionRepo;
import com.spring.bank.repository.UserRepo;
import com.spring.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    TransactionRepo transactionRepo;
    @Autowired
    BankAccountRepo bankAccountRepo;

    @Override
    public Boolean createTransaction(Integer loggedId, Transaction transaction) {
        User user = userRepo.findByid(loggedId);
        if (user.getBankAccount() != null
                && transaction.getTransactionSum() != null
                && transaction.getTransactionType() != null
                && (transaction.getTransactionType().equals("deposit")
                || transaction.getTransactionType().equals("withdraw"))) {
            LocalDate date = LocalDate.now();
            transaction.setCreatedAt(date);
            transaction.setUser(user);
            transaction.setTransactionStatus("pending");
            transactionRepo.save(transaction);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean cancelTransaction(Integer loggedId, Transaction transaction) {
        Transaction transaction1 = transactionRepo.findByid(transaction.getId());
        User user = userRepo.findByid(loggedId);
        if (transaction1.getTransactionStatus() != null && transaction1.getTransactionStatus().equals("pending")){
            if (transaction1.getUser().getId() == user.getId()) {
                transactionRepo.delete(transaction1);
            }
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public List<Transaction> getAllPendingTransactions(Integer loggedId) {
        User user = userRepo.findByid(loggedId);
        if (!user.getRole().name().equals("USER")) {
            List<Transaction> transactionsByStatus = transactionRepo.findAllByTransactionStatus("pending");
            for (Transaction tr : transactionsByStatus) {
                tr.setUser(null);
            }
            return transactionsByStatus;
        } else {
            return null;
        }
    }

    @Override
    public Boolean acceptTransaction(Integer loggedId, Transaction transaction) {
        BankAccount bankAccount;
        User user = userRepo.findByid(loggedId);
        if (user.getRole().name().equals("ADMIN")) {

            Transaction transaction1 = transactionRepo.findByid(transaction.getId());
            if (transaction1 != null) {
                bankAccount = transaction1.getUser().getBankAccount();
            }
            else {
                return false;
            }
            if (bankAccount != null) {
                transaction1.setTransactionStatus("approved");
                if (transaction1.getTransactionType().equals("deposit")) {
                    bankAccount.setBalance(bankAccount.getBalance() + transaction1.getTransactionSum());
                } else if (transaction1.getTransactionType().equals("withdraw")) {
                    bankAccount.setBalance(bankAccount.getBalance() - transaction1.getTransactionSum());
                }
                transactionRepo.save(transaction1);
                bankAccountRepo.save(bankAccount);
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }


}
