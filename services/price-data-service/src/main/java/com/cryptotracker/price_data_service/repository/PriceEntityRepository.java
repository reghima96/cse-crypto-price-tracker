package com.cryptotracker.price_data_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PriceEntityRepository extends JpaRepository<PriceEntity, UUID> {
    Page<PriceEntity> findBySymbolIn(List<String> symbols, Pageable pageable);

    @Query("SELECT p FROM PriceEntity p WHERE p.symbol IN :symbols AND p.timestamp BETWEEN :startDate AND :endDate ORDER BY p.timestamp DESC")
    Page<PriceEntity> findPricesBySymbolsAndDateRange(
            @Param("symbols") List<String> symbols,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    // Find latest price per symbol
    @Query("SELECT p FROM PriceEntity p WHERE p.timestamp = (SELECT MAX(p2.timestamp) FROM PriceEntity p2 WHERE p2.symbol = p.symbol)")
    List<PriceEntity> findLatestPrices();
}

