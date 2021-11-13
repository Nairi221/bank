package com.spring.bank.repository;

import com.spring.bank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepo extends JpaRepository<User, Integer> {

    User findByUsername(String username);
    User findByid(Integer id);


}
