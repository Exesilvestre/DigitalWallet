package com.example.cards_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CardsServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardsServerApplication.class, args);
	}

}
