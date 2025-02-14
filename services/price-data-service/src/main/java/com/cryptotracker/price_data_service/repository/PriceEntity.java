package com.cryptotracker.price_data_service.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "prices", indexes = @Index(columnList = "symbol"))
public class PriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String symbol;

    @Column(precision = 19, scale = 8)
    private BigDecimal price;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;

    public PriceEntity() {
    }

    public PriceEntity(String symbol, BigDecimal price, LocalDateTime timestamp) {
        this.symbol = symbol;
        this.price = price;
        this.timestamp = timestamp;
    }
}