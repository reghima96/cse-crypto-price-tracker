package com.cryptotracker.price_data_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceEntityRepository extends JpaRepository<PriceEntity, UUID> {
    List<PriceEntity> findBySymbolIn(List<String> symbols);
}
