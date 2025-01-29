package com.cryptotracker.price_data_service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, UUID> {
  List<Cryptocurrency> findBySymbolIn(List<String> symbols);
  Optional<Cryptocurrency> findBySymbolIgnoreCase(String symbol);
  boolean existsByCoinGeckoId(String coinGeckoId);
}
