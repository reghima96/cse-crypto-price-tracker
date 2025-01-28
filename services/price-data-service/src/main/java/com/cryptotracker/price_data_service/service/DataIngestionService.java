package com.cryptotracker.price_data_service.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.cryptotracker.price_data_service.repository.Cryptocurrency;
import com.cryptotracker.price_data_service.repository.CryptocurrencyRepository;
import com.cryptotracker.price_data_service.repository.PriceEntity;

@Service
public class DataIngestionService {

    private static final Logger logger = LoggerFactory.getLogger(DataIngestionService.class);

    private final PriceService priceService;
    private final CryptocurrencyRepository cryptocurrencyRepository;
    private final WebClient webClient;

    @Value("${coingecko.api.base-url}")
    private String baseUrl;

    public DataIngestionService(PriceService priceService, CryptocurrencyRepository cryptocurrencyRepository, WebClient.Builder webClientBuilder) {
        this.priceService = priceService;
        this.cryptocurrencyRepository = cryptocurrencyRepository;
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Scheduled(fixedDelay = 300000) // Wait 5 minutes between runs
    public void fetchPriceData() {
        List<Cryptocurrency> cryptocurrencies = cryptocurrencyRepository.findAll();

        if (cryptocurrencies.isEmpty()) {
            logger.warn("No cryptocurrencies configured for fetching.");
            return;
        }

        // Build the CoinGecko API request with all cryptocurrency IDs
        String ids = cryptocurrencies.stream()
                .map(Cryptocurrency::getCoinGeckoId)
                .collect(Collectors.joining(","));

        try {
            // Fetch prices in a batch
            Map<String, Map<String, BigDecimal>> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/simple/price")
                            .queryParam("ids", ids)
                            .queryParam("vs_currencies", "eur")
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Map<String, BigDecimal>>>() {
                    })
                    .block();

            // Save prices to the database
            if (response != null) {
                savePrices(cryptocurrencies, response);
            } else {
                logger.warn("Empty response received from CoinGecko API.");
            }
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                logger.warn("Rate limit hit. Consider implementing a backoff strategy: {}", e.getMessage());
            } else {
                logger.error("HTTP error while fetching prices: {}", e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error fetching price data: {}", e.getMessage());
        }
    }

    private void savePrices(List<Cryptocurrency> cryptocurrencies, Map<String, Map<String, BigDecimal>> response) {
        for (Cryptocurrency crypto : cryptocurrencies) {
            String coinGeckoId = crypto.getCoinGeckoId();
            if (response.containsKey(coinGeckoId) && response.get(coinGeckoId).containsKey("eur")) {
                BigDecimal priceValue = response.get(coinGeckoId).get("eur");
                PriceEntity priceEntity = new PriceEntity(
                        crypto.getSymbol(),
                        priceValue,
                        LocalDateTime.now());
                priceService.save(priceEntity);
                logger.info("Saved price for {}: {}", crypto.getSymbol(), priceValue);
            } else {
                logger.warn("No price data available for cryptocurrency: {}", crypto.getSymbol());
            }
        }
    }
}