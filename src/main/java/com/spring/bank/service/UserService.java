package com.spring.bank.service;


import com.spring.bank.entity.Transaction;
import com.spring.bank.entity.User;

import java.util.Set;

public interface UserService {

    User createUser(User user);
    User getUserByUsername(String username);
    User getUserById(Integer userId);
    User checkLogin(User user);
    Set<Transaction> getUserHistory(Integer userId);
    User changeRoleOfUser(Integer loggedId, User toBeChangedUser);

}
