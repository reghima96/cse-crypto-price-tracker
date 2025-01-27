package com.cryptotracker.price_data_service.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.cryptotracker.price_data_service.repository.Cryptocurrency;
import com.cryptotracker.price_data_service.repository.CryptocurrencyRepository;
import com.cryptotracker.price_data_service.repository.PriceEntity;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class DataIngestionService {

    private static final Logger logger = LoggerFactory.getLogger(DataIngestionService.class);

    private final PriceService priceService;
    private final RestTemplate restTemplate;
    private final CryptocurrencyRepository cryptocurrencyRepository;

    public DataIngestionService(PriceService priceService, RestTemplate restTemplate, CryptocurrencyRepository cryptocurrencyRepository) {
        this.priceService = priceService;
        this.restTemplate = restTemplate;
        this.cryptocurrencyRepository = cryptocurrencyRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void fetchPriceData() {
        List<Cryptocurrency> cryptocurrencies = cryptocurrencyRepository.findAll();
        for (Cryptocurrency crypto : cryptocurrencies) {
            String symbol = crypto.getSymbol();
            String coinGeckoId = crypto.getCoinGeckoId();
            String apiUrl = "https://api.coingecko.com/api/v3/simple/price?ids=" + coinGeckoId + "&vs_currencies=usd";
            try {
                JsonNode root = restTemplate.getForObject(apiUrl, JsonNode.class);

                if (root != null && root.has(coinGeckoId) && root.get(coinGeckoId).has("usd")) {
                    BigDecimal priceValue = BigDecimal.valueOf(root.get(coinGeckoId).get("usd").asDouble());
                    PriceEntity price = new PriceEntity(symbol, priceValue, LocalDateTime.now());
                    priceService.save(price);
                    logger.info("Current price of {} : {}", symbol, price.getPrice());
                } else {
                    logger.error("Could not extract rate for {}", symbol);
                }
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                    logger.warn("Rate limit hit for {}: {}", symbol, e.getMessage());
                    //TODO
                    // Implement a backoff strategy here (e.g., wait and retry)
                } else {
                    logger.error("HTTP error fetching price for {}: {}", symbol, e.getMessage());
                }
            } catch (Exception e) {
                logger.error("Error fetching price data for {}: {}", symbol, e.getMessage());
            }
        }
    }
}