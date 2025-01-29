package com.cryptotracker.price_data_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cryptotracker.price_data_service.repository.Cryptocurrency;
import com.cryptotracker.price_data_service.service.AdminCryptocurrencyService;

@RestController
@RequestMapping("/admin/cryptocurrencies")
public class AdminCryptocurrencyController {

    private final AdminCryptocurrencyService adminCryptocurrencyService;

    public AdminCryptocurrencyController(AdminCryptocurrencyService adminCryptocurrencyService) {
        this.adminCryptocurrencyService = adminCryptocurrencyService;
    }

    @PostMapping
    public ResponseEntity<Cryptocurrency> addCryptocurrency(@RequestBody Cryptocurrency crypto) {
        Cryptocurrency savedCrypto = adminCryptocurrencyService.addCryptocurrency(crypto.getSymbol(), crypto.getCoinGeckoId());
        return ResponseEntity.ok(savedCrypto);
    }
}
