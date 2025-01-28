package com.cryptotracker.price_data_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceEntityRepository extends JpaRepository<PriceEntity, UUID> {
    Page<PriceEntity> findBySymbolIn(List<String> symbols, Pageable pageable);
}
