package com.cryptotracker.user_service.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cryptotracker.user_service.repository.Price;
import com.cryptotracker.user_service.service.PriceService;

@RestController
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping("/api/prices")
    public ResponseEntity<List<Price>> getPrices(
            @RequestParam(value = "symbol", required = false) String symbol,
            @RequestParam(value = "startDate", required = false) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) LocalDateTime endDate) {

        List<Price> prices;
        if (symbol != null && startDate != null && endDate != null) {
            prices = priceService.getPricesBySymbolAndDateRange(symbol, startDate, endDate);
        } else if (symbol != null) {
            prices = priceService.getPricesBySymbol(symbol);
        } else {
            prices = priceService.getAllPrices();
        }

        return ResponseEntity.ok(prices);
    }
}



// @RestController
// public class PriceController {

//     private final PriceService priceService;

//     public PriceController(PriceService priceService) {
//         this.priceService = priceService;
//     }

//     @GetMapping("/api/prices")
//     public ResponseEntity<List<Price>> getPrices(
//             @RequestParam(value = "symbols", required = false) String symbols,
//             @RequestParam(value = "period", required = false) String period) {

//         List<Price> prices = new java.util.ArrayList<>();
//         LocalDateTime now = LocalDateTime.now();

//         if (symbols != null) {
//             List<String> symbolList = Arrays.stream(symbols.split(",")).collect(Collectors.toList());
//             for (String symbolItem : symbolList) {
//                 if ("week".equalsIgnoreCase(period)) {
//                     LocalDateTime startDate = now.minus(7, ChronoUnit.DAYS);
//                     prices.addAll(priceService.getPricesForWeek(symbolItem, startDate, now));
//                 } else {
//                     prices.addAll(priceService.getPricesBySymbol(symbolItem));
//                 }
//             }
//         } else {
//             prices = priceService.getAllPrices();
//         }

//         return ResponseEntity.ok(prices);
//     }
// }