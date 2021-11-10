package com.spring.bank.controllers;

import com.spring.bank.entity.BankAccount;
import com.spring.bank.entity.Transaction;
import com.spring.bank.entity.User;
import com.spring.bank.enums.Role;
import com.spring.bank.repository.BankAccountDAO;
import com.spring.bank.repository.TransactionDAO;
import com.spring.bank.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.util.EnumUtils;

import java.time.LocalDate;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController1 {

  @Autowired UserDAO userDAO;
  @Autowired BankAccountDAO bankAccountDAO;
  @Autowired TransactionDAO transactionDAO;

  @GetMapping("/{id}")
  public User getUserById(@PathVariable("id") int id) {
    return userDAO.findByid(id);
  }

  @PostMapping("/register")
  public ResponseEntity<User> createUser(@RequestBody User user) {

    if (user.getUsername() == null || user.getUsername().trim().equals("")) {
      // some error text
      return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
    } else if (user.getFirstName() == null || user.getFirstName().trim().equals("")) {
      // some error text
      return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
    } else if (user.getLastName() == null || user.getLastName().trim().equals("")) {
      // some error text
      return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
    } else if (user.getPassword() == null || user.getPassword().trim().equals("")) {
      // some error text
      return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
    } else if (userDAO.findByUsername(user.getUsername()) != null) {
      // some error text (user with such username already exists)
      return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
    } else {
      // password encoding
      String encodedString = Base64.getEncoder().encodeToString(user.getPassword().getBytes());

      user.setPassword(encodedString);
      LocalDate date = LocalDate.now();
      user.setCreatedAt(date);
      user.setRole(Role.USER);
      User user1 = userDAO.save(user);
      return new ResponseEntity<>(user1, HttpStatus.CREATED);
    }
  }

  @PostMapping("/login")
  public ResponseEntity<User> loginUser(@RequestBody User user) {

    User logUser = userDAO.findByUsername(user.getUsername());
    if (logUser != null) {
      String userPassword = Base64.getEncoder().encodeToString(user.getPassword().getBytes());
      if (userPassword.equals(logUser.getPassword())) {
        // success
        return new ResponseEntity<>(logUser, HttpStatus.OK);
      } else {
        // no such user exists, try again
        return new ResponseEntity<>(logUser, HttpStatus.UNAUTHORIZED);
      }
    } else {
      // no such user exists, try again
      return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
  }



  @GetMapping("/userHistory/{id}")
  public ResponseEntity<Set<Transaction>> userHistoryPost(@PathVariable Integer id) {

    User user1 = userDAO.findByid(id);
    if (user1.getTransactions() == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    } else {
      Set<Transaction> set = user1.getTransactions();
      for (Transaction tr : set){
        tr.setUser(null);
      }
      return new ResponseEntity<>(set, HttpStatus.OK);
    }
  }

  /**
   * sarqaca
   * @param id .
   * @param transaction .
   * @return .
   */
  @PostMapping("/cancel_transaction/{id}")
  public ResponseEntity<String> cancelTransaction(
      @PathVariable Integer id, @RequestBody Transaction transaction) {
    User user = userDAO.findByid(id);
    if (transaction.getTransactionStatus() != null && transaction.getTransactionStatus().equals("pending")){
      Transaction transaction1 = transactionDAO.findByid(transaction.getId());
      transactionDAO.delete(transaction1);
      return new ResponseEntity<>("transaction canceled, success", HttpStatus.OK);
    }
    else{
      return new ResponseEntity<>("problem occurred", HttpStatus.NOT_ACCEPTABLE);
    }
  }

  /**
   * sarqac chi
   * @param model .
   * @param id .
   * @return .
   */
  @GetMapping("/edit_users/{id}")
  public String userEditGet(ModelMap model, @PathVariable Integer id) {
    User user = userDAO.findByid(id);
    model.addAttribute("user", user);
    return "/edit_users";
  }

  /**
   *
   * @param user .
   * @return.
   */

  @PutMapping("/edit_users/{id}")
  public ResponseEntity<User> userEditPost(@PathVariable Integer id, @RequestBody User user) {

    User loggedUser = userDAO.findByid(id);
    if (loggedUser.getRole().name().equals("ADMIN")) {
      User userToChangeRole = userDAO.findByUsername(user.getUsername());
      if (userToChangeRole != null) {
        userToChangeRole.setRole(user.getRole());
        userDAO.save(userToChangeRole);
        return new ResponseEntity<>(user, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
      }
    }
    else{
      return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }
  }

  /**
   * sarqaca
   * @param id .
   * @return .
   */
  // admin@ mtnuma ira ej , u tenuma en transactioner@ voronc status@ = pending
  @GetMapping("/accept_transactions/{id}")
  public ResponseEntity<List<Transaction>> acceptTransactionsGet(@PathVariable Integer id) {
    User user = userDAO.findByid(id);
    if (!user.getRole().name().equals("USER")) {
      List<Transaction> transactionsByStatus = transactionDAO.findAllByTransactionStatus("pending");
      for (Transaction tr : transactionsByStatus) {
        tr.setUser(null);
      }
      return new ResponseEntity<>(transactionsByStatus, HttpStatus.OK);
    }
    else{
      return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }
  }

  /**
   *  sarqaca
   * @param id .
   * @param transaction .
   * @return .
   */
  // stexovel admin@ hastatuma transakcian
  @PostMapping("/accept_transactions/{id}")
  public ResponseEntity<String> acceptTransactionsPost(
      @PathVariable Integer id, @RequestBody Transaction transaction) {

    User user = userDAO.findByid(id);
    if (user.getRole().name().equals("ADMIN")) {
      Transaction transaction1 = transactionDAO.findByid(transaction.getId());
      BankAccount bankAccount = transaction1.getUser().getBankAccount();

      if (bankAccount != null) {
        transaction1.setTransactionStatus("approved");
        if (transaction1.getTransactionType().equals("deposit")) {
          bankAccount.setBalance(bankAccount.getBalance() + transaction1.getTransactionSum());
        } else if (transaction1.getTransactionType().equals("withdrew")) {
          bankAccount.setBalance(bankAccount.getBalance() - transaction1.getTransactionSum());
        }
        transactionDAO.save(transaction1);
        bankAccountDAO.save(bankAccount);
      } else {
        // some text that user does not have bank account
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
      }
    }
    else{
      return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }
    return new ResponseEntity<>("approved", HttpStatus.OK);
  }

  /**
   * user create transaction
   *
   * @param id .
   * @param transaction .
   * @return .
   */
  @PostMapping("/create_transaction/{id}")
  public ResponseEntity<Transaction> createTransaction(
      @PathVariable Integer id, @RequestBody Transaction transaction) {
    LocalDate date = LocalDate.now();
    User user = userDAO.findByid(id);
    Set<Transaction> set = new HashSet<>();
    if (user.getBankAccount() != null) {
      transaction.setTransactionSum(transaction.getTransactionSum());
      transaction.setUser(user);
      transaction.setCreatedAt(date);
      transaction.setTransactionStatus("pending");
      transaction.setTransactionType(transaction.getTransactionType());
      set.add(transaction);
      user.setTransactions(set);
      transactionDAO.save(transaction);
      userDAO.save(user);
    } else {
      return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }
    return new ResponseEntity<>(transaction, HttpStatus.OK);
  }
}
