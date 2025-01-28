package com.cryptotracker.price_data_service.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cryptotracker.price_data_service.repository.PriceEntity;
import com.cryptotracker.price_data_service.service.PriceService;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping
    public ResponseEntity<Page<PriceEntity>> getPrices(@RequestParam(value = "symbols", required = false) String symbols, @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PriceEntity> prices;
        if (symbols != null) {
            List<String> symbolList = List.of(symbols.split(","));
            prices = priceService.getPricesBySymbols(symbolList, pageable);
        } else {
            prices = priceService.getAllPrices(pageable);
        }

        return prices.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(prices);
    }
}
