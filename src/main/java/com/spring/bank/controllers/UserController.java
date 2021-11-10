//package com.spring.bank.controllers;
//
//import com.spring.bank.entity.BankAccount;
//import com.spring.bank.entity.Transaction;
//import com.spring.bank.entity.User;
//import com.spring.bank.enums.Role;
//import com.spring.bank.repository.BankAccountDAO;
//import com.spring.bank.repository.TransactionDAO;
//import com.spring.bank.repository.UserDAO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Set;
//
//@Controller
//public class UserController {
//
//    @Autowired UserDAO userDAO;
//    @Autowired BankAccountDAO bankAccountDAO;
//    @Autowired TransactionDAO transactionDAO;
//
//
//    @GetMapping("/")
//    public String welcomePage(ModelMap model){
//        User user = new User();
//        model.addAttribute("user", user);
//        return "/welcome";
//    }
//
//    @GetMapping("/sign_in")
//    public String signInGet(ModelMap model){
//        User user = new User();
//        model.addAttribute("user", user);
//        return "/user_sign_in";
//    }
//
//    @PostMapping("/sign_in")
//    public String signInPost(ModelMap model,
//                             @ModelAttribute("user") User user){
//
//        if (user.getUsername() == null || user.getUsername().trim().equals("")){
//            //some error text
//            return "user_sign_in";
//        }
//        else if(user.getFirstName() == null || user.getFirstName().trim().equals("")){
//            //some error text
//            return "user_sign_in";
//        }
//        else if(user.getLastName() == null || user.getLastName().trim().equals("")){
//            //some error text
//            return "user_sign_in";
//        }
//        else if(user.getPassword() == null || user.getPassword().trim().equals("")){
//            //some error text
//            return "user_sign_in";
//        }
//        else if(userDAO.findByUsername(user.getUsername()) != null){
//            //some error text (user with such username already exists)
//            return "user_sign_in";
//        }
//        else{
//            LocalDate date = LocalDate.now();
//            user.setCreatedAt(date);
//            user.setRole(Role.USER);
//            userDAO.save(user);
//            return "/welcome";
//        }
//    }
//
//    @GetMapping("/login")
//    public String loginGet(ModelMap model){
//        User user = new User();
//        model.addAttribute("user", user);
//        return "/login";
//    }
//
//    @PostMapping("/login")
//    public String loginPost(ModelMap model,
//                             @ModelAttribute("user") User user){
//
//        User logUser = userDAO.findByUsername(user.getUsername());
//        if (logUser != null){
//            if (user.getPassword().equals(logUser.getPassword())){
//                //success
//                model.addAttribute("user", logUser);
////                return "/user_page";
//                return "redirect:/user_page/" + logUser.getId();
//            }
//            else {
//                //no such user exists, try again
//                return "login";
//            }
//        }
//        else {
//            //no such user exists, try again
//            return "login";
//        }
//    }
//
//    @GetMapping("/user_page/{id}")
//    public String userPageGet(ModelMap model, @PathVariable Integer id){
//        User user = userDAO.findByid(id);
//        model.addAttribute("user", user);
//        return "/user_page";
//    }
//
//    @GetMapping("/create_bank_account/{id}")
//    public String createBankAccGet(ModelMap model, @PathVariable Integer id,
//                                   @RequestParam(name="username", required=false) String username,
//                                   @RequestParam(name="user_id", required=false) String userID){
//
//        User user = userDAO.findByid(id);
//        model.addAttribute("user", user);
//
//        return "/create_bank_account";
//    }
//
//    @PostMapping("/create_bank_account/{id}")
//    public String createBankAccPost(ModelMap model, @PathVariable Integer id,
//                                    @RequestParam(name="username", required=false) String username,
//                                    @RequestParam(name="user_id", required=false) String userID){
//
//
//        User userToEdit = userDAO.findByUsername(username);
//        if (userToEdit == null){
//            //no user with this username
//            return "redirect:/create_bank_account/" + id;
//        }
//        if (userToEdit.getBankAccount() == null) {
//            BankAccount bankAccount = new BankAccount();
//            LocalDate date = LocalDate.now();
//            bankAccount.setCreatedAt(date);
//            bankAccount.setBalance(0);
//            bankAccount.setUser(userToEdit);
//            bankAccountDAO.save(bankAccount);
//            userToEdit.setBankAccount(bankAccount);
//            userDAO.save(userToEdit);
//            System.out.println("hello");
//        }
//        else{
//            //some error that bank account already exists
//        }
//
//        return "redirect:/create_bank_account/" + id;
//    }
//
//    @GetMapping("/deposit/{id}")
//    public String depositGet(ModelMap model, @PathVariable Integer id){
//
//        User user = userDAO.findByid(id);
//        model.addAttribute("user", user);
//        return "/deposit";
//    }
//
//    @PostMapping("/deposit/{id}")
//    public String depositPost(ModelMap model, @PathVariable Integer id,
//                              @RequestParam(name="deposit_sum", required=false) Integer depositSum,
//                              @RequestParam(name="user_id", required=false) String userID){
//
//        User user = userDAO.findByid(id);
//        if (user.getBankAccount() != null) {
//            Transaction transaction = new Transaction();
//            LocalDate date = LocalDate.now();
//            transaction.setCreatedAt(date);
//            transaction.setTransactionType("deposit");
//            transaction.setUser(user);
//            transaction.setTransactionStatus("pending");
//            transaction.setTransactionSum(depositSum);
//            transactionDAO.save(transaction);
//        }
//        else {
//            //some text that user doesnt have bank account, so cant do transactions
//        }
//        model.addAttribute("user", user);
//        return "redirect:/user_page/" + id;
//    }
//
//    @GetMapping("/withdrew/{id}")
//    public String withdrewGet(ModelMap model, @PathVariable Integer id){
//
//        User user = userDAO.findByid(id);
//        model.addAttribute("user", user);
//        return "/withdrew";
//    }
//
//    @PostMapping("/withdrew/{id}")
//    public String withdrewPost(ModelMap model, @PathVariable Integer id,
//                              @RequestParam(name="withdrew_sum", required=false) Integer depositSum,
//                              @RequestParam(name="user_id", required=false) String userID){
//
//        User user = userDAO.findByid(id);
//        if (user.getBankAccount() != null) {
//        Transaction transaction = new Transaction();
//        LocalDate date = LocalDate.now();
//        transaction.setCreatedAt(date);
//        transaction.setTransactionType("withdrew");
//        transaction.setUser(user);
//        transaction.setTransactionStatus("pending");
//        transaction.setTransactionSum(depositSum);
//        transactionDAO.save(transaction);
//        }
//        else {
//            //some text that user doesnt have bank account, so cant do transactions
//        }
//
//        model.addAttribute("user", user);
//        return "redirect:/user_page/" + id;
//    }
//
//    @GetMapping("/userHistory/{id}")
//    public String userHistoryGet(ModelMap model, @PathVariable Integer id){
//
//        Transaction transaction = new Transaction();
//        User user = userDAO.findByid(id);
//        Set<Transaction> trSet = user.getTransactions();
//        model.addAttribute("user", user);
//        model.addAttribute("trSet", trSet);
//        model.addAttribute("transaction", transaction);
//        return "/see_user_history";
//    }
//
//    @PostMapping("/userHistory/{id}")
//    public String userHistoryPost(ModelMap model, @PathVariable Integer id,
//                                  @RequestParam(name="user_id", required=false) Integer userID,
//                                  @RequestParam(name="transaction_id", required=false) Integer transactionId){
//
//        User user = userDAO.findByid(id);
//        Transaction transaction = transactionDAO.getById(transactionId);
//        transactionDAO.delete(transaction);
//        model.addAttribute("user", user);
//        return "redirect:/user_page/" + id;
//    }
//
//    @GetMapping("/edit_users/{id}")
//    public String userEditGet(ModelMap model, @PathVariable Integer id){
//        User user = userDAO.findByid(id);
//        model.addAttribute("user", user);
//        return "/edit_users";
//    }
//
//    @PostMapping("/edit_users/{id}")
//    public String userEditPost(ModelMap model, @PathVariable Integer id,
//                               @RequestParam(name="user_id", required=false) Integer userID,
//                               @RequestParam(name="username", required=false) String username,
//                               @RequestParam(name="role", required=false) String roleName){
//
//
//        User user = userDAO.findByid(id);
//        model.addAttribute("user", user);
//
//        User userToChangeRole = userDAO.findByUsername(username);
//
//
//        if (userToChangeRole != null) {
//            Role role1 = Role.valueOf(roleName);
//            userToChangeRole.setRole(role1);
//            userDAO.save(userToChangeRole);
//        }
//        else{
//            //some text that there is no user with such username
//        }
//        return "redirect:/user_page/" + id;
//    }
//
//    @GetMapping("/accept_transactions/{id}")
//    public String acceptTransactionsGet(ModelMap model, @PathVariable Integer id){
//        User user = userDAO.findByid(id);
//        model.addAttribute("user", user);
//
//        List<Transaction> transactionsByStatus = transactionDAO.findAllByTransactionStatus("pending");
//        model.addAttribute("transactions", transactionsByStatus);
//
//        return "/accept_transactions";
//    }
//
//    @PostMapping("/accept_transactions/{id}")
//    public String acceptTransactionsPost(ModelMap model, @PathVariable Integer id,
//                                         @RequestParam(name="user_id", required=false) Integer userID,
//                                         @RequestParam(name="transaction_id", required=false) Integer transactionId){
//
//        User user = userDAO.findByid(id);
//        model.addAttribute("user", user);
//        Transaction transaction = transactionDAO.findByid(transactionId);
//        BankAccount bankAccount = transaction.getUser().getBankAccount();
//        transaction.setTransactionStatus("approved");
//        if (bankAccount != null) {
//            if (transaction.getTransactionType().equals("deposit")) {
//                bankAccount.setBalance(bankAccount.getBalance() + transaction.getTransactionSum());
//            } else if (transaction.getTransactionType().equals("withdrew")) {
//                bankAccount.setBalance(bankAccount.getBalance() - transaction.getTransactionSum());
//            }
//            transactionDAO.save(transaction);
//            bankAccountDAO.save(bankAccount);
//        }
//        else {
//            //some text that user doesnt have bank account
//        }
//        return "redirect:/user_page/" + id;
//    }
//
//}
