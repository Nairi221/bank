package com.spring.bank.repository;

import com.spring.bank.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepo extends JpaRepository<BankAccount, Integer> {



}
