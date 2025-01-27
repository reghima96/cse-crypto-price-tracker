package com.cryptotracker.price_data_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cryptotracker.price_data_service.repository.PriceEntity;
import com.cryptotracker.price_data_service.service.PriceService;

@RestController
public class DataIngestionController {

    private final PriceService priceService;

    public DataIngestionController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping("/api/prices")
    public ResponseEntity<List<PriceEntity>> getPrices(
            @RequestParam(value = "symbols", required = false) String symbols) {

        List<PriceEntity> prices;

        if (symbols != null) {
            List<String> symbolList = java.util.Arrays.stream(symbols.split(",")).toList();
            prices = priceService.getPricesBySymbols(symbolList);
        } else {
            prices = priceService.getAllPrices();
        }

        if (prices.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(prices);
    }
}