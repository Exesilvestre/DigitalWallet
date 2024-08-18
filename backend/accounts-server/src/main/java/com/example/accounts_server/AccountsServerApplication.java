package com.example.accounts_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AccountsServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsServerApplication.class, args);
	}

}
