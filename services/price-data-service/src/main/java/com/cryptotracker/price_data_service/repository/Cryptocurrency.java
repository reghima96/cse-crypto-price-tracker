package com.cryptotracker.price_data_service.repository;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "cryptocurrencies")
public class Cryptocurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String symbol;

    @Column(unique = true, nullable = false)
    private String coinGeckoId;

    // Constructors, getters, and setters
    public Cryptocurrency() {
    }

    public Cryptocurrency(String symbol, String coinGeckoId) {
        this.symbol = symbol;
        this.coinGeckoId = coinGeckoId;
    }

}
