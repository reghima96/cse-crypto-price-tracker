package com.cryptotracker.price_data_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cryptotracker.price_data_service.repository.PriceEntity;

public record PriceDto(String symbol, BigDecimal price, LocalDateTime timestamp) {
    public static PriceDto fromEntity(PriceEntity entity) {
        return new PriceDto(entity.getSymbol(), entity.getPrice(), entity.getTimestamp());
    }
}
