package com.cryptotracker.price_data_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, UUID> {
  List<Cryptocurrency> findBySymbolIn(List<String> symbols);

  boolean existsByCoinGeckoId(String coinGeckoId);

  boolean existsBySymbol(String symbol);
}
