package com.cryptotracker.price_data_service.dto;

import lombok.Data;

@Data
public class CryptocurrencyDto {
    private String symbol;
    private String coinGeckoId;
} 