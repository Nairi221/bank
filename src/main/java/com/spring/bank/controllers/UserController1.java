package com.spring.bank.controllers;

import com.spring.bank.entity.Transaction;
import com.spring.bank.entity.User;
import com.spring.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController1 {

  @Autowired UserService userService;

  /**
   * stugaca good
   *
   * @param user .
   * @return .
   */
  @PostMapping("/register")
  public ResponseEntity<User> createUser(@RequestBody User user) {

    if (user.getUsername() == null || user.getUsername().trim().equals("")) {
      return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
    } else if (user.getFirstName() == null || user.getFirstName().trim().equals("")) {
      return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
    } else if (user.getLastName() == null || user.getLastName().trim().equals("")) {
      return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
    } else if (user.getPassword() == null || user.getPassword().trim().equals("")) {
      return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
    } else if (userService.getUserByUsername(user.getUsername()) != null) {
      return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
    } else {
      User registeredUser = userService.createUser(user);
      return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }
  }

  @PostMapping("/login")
  public ResponseEntity<User> loginUser(@RequestBody User user) {

    User logUser = userService.checkLogin(user);
    if (logUser != null) {
      return new ResponseEntity<>(logUser, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
  }

  @GetMapping("/userHistory/{id}")
  public ResponseEntity<Set<Transaction>> userHistoryPost(@PathVariable Integer id) {
    Set<Transaction> set = userService.getUserHistory(id);
    if (set == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    } else {
      return new ResponseEntity<>(set, HttpStatus.OK);
    }
  }

  /**
   *
   * @param user .
   * @return .
   */
  @PutMapping("/edit_users/{id}")
  public ResponseEntity<String> userEditPost(@PathVariable Integer id, @RequestBody User user) {
    User toBeChanged = userService.changeRoleOfUser(id, user);
    if (toBeChanged == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    } else {
      return new ResponseEntity<>("text ", HttpStatus.OK);
    }
  }
}
