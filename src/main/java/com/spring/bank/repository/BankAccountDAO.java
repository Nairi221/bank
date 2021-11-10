package com.spring.bank.repository;

import com.spring.bank.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountDAO extends JpaRepository<BankAccount, Integer> {



}
