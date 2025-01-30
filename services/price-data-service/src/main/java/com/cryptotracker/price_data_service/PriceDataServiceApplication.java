package com.cryptotracker.price_data_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan("com.cryptotracker.price_data_service.repository")
@EnableJpaRepositories("com.cryptotracker.price_data_service.repository")
@EnableScheduling
public class PriceDataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriceDataServiceApplication.class, args);
	}

}
