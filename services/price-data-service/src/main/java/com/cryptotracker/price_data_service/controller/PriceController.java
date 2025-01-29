package com.cryptotracker.price_data_service.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cryptotracker.price_data_service.dto.PriceDto;
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
    public ResponseEntity<Page<PriceDto>> getPrices(
            @RequestParam Optional<String> symbols,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<PriceEntity> prices = symbols.isPresent()
                ? priceService.getPricesBySymbols(parseSymbols(symbols), pageable)
                : priceService.getAllPrices(pageable);

        return prices.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(prices.map(PriceDto::fromEntity));
    }

    // Fetch the latest price per symbol
    @GetMapping("/latest")
    public ResponseEntity<List<PriceEntity>> getLatestPrices() {
        List<PriceEntity> latestPrices = priceService.getLatestPrices();
        return latestPrices.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(latestPrices);
    }

    @GetMapping("/history")
    public ResponseEntity<Page<PriceEntity>> getHistoricalPrices(
            @RequestParam List<String> symbols,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        Pageable pageable = PageRequest.of(page, size);

        Page<PriceEntity> historicalPrices = priceService.getHistoricalPrices(symbols, start, end, pageable);
        return ResponseEntity.ok(historicalPrices);
    }

    private List<String> parseSymbols(Optional<String> symbols) {
        return symbols.map(s -> Arrays.asList(s.split(","))).orElse(List.of());
    }
}
