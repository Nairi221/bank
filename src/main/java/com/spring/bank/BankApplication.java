package com.spring.bank;

import com.spring.bank.entity.User;
import com.spring.bank.enums.Role;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankApplication {


	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
		User user = new User();
		user.setRole(Role.ADMIN);
	}

}
