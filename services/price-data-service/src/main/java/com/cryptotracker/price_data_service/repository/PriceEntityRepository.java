package com.cryptotracker.price_data_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceEntityRepository extends JpaRepository<PriceEntity, UUID> {

        // For dashboard charts
        @Query("SELECT p FROM PriceEntity p " +
                        "WHERE p.symbol = :symbol " +
                        "AND p.timestamp >= :since " +
                        "ORDER BY p.timestamp ASC")
        List<PriceEntity> findRecentPricesBySymbolAndTimeRange(
                        @Param("symbol") String symbol,
                        @Param("since") LocalDateTime since);

        // For export functionality
        List<PriceEntity> findByTimestampGreaterThanEqual(LocalDateTime since);

        // For latest prices
        @Query("SELECT p FROM PriceEntity p " +
                        "WHERE p.timestamp = (SELECT MAX(p2.timestamp) FROM PriceEntity p2 WHERE p2.symbol = p.symbol)")
        List<PriceEntity> findLatestPrices();
}
