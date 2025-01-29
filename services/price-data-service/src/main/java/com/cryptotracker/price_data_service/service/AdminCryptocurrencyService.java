package com.cryptotracker.price_data_service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cryptotracker.price_data_service.repository.Cryptocurrency;
import com.cryptotracker.price_data_service.repository.CryptocurrencyRepository;

@Service
public class AdminCryptocurrencyService {

    private final CryptocurrencyRepository cryptocurrencyRepository;

    public AdminCryptocurrencyService(CryptocurrencyRepository cryptocurrencyRepository) {
        this.cryptocurrencyRepository = cryptocurrencyRepository;
    }

    @Transactional
    public Cryptocurrency addCryptocurrency(String symbol, String coinGeckoId) {
        if (cryptocurrencyRepository.findBySymbolIgnoreCase(symbol).isPresent()) {
            throw new IllegalArgumentException("Symbol already exists: " + symbol);
        }
        if (cryptocurrencyRepository.existsByCoinGeckoId(coinGeckoId)) {
            throw new IllegalArgumentException("CoinGecko ID already exists: " + coinGeckoId);
        }
        Cryptocurrency crypto = new Cryptocurrency(symbol.toUpperCase(), coinGeckoId);
        return cryptocurrencyRepository.save(crypto);
    }
}
