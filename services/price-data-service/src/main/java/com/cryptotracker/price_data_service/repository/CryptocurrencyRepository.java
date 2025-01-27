package com.cryptotracker.price_data_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, UUID> {
}