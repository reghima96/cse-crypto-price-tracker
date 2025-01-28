package com.cryptotracker.price_data_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PriceDataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriceDataServiceApplication.class, args);
	}

}
