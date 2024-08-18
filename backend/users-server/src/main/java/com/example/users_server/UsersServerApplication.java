package com.example.users_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UsersServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersServerApplication.class, args);
	}

}
