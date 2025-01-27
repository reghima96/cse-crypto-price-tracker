package com.cryptotracker.user_service.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cryptotracker.user_service.repository.Price;

@Service
public class PriceService {

    private static final Logger logger = LoggerFactory.getLogger(PriceService.class);
    private final RestTemplate restTemplate;
    private final String dataIngestionServiceUrl;

    public PriceService(RestTemplate restTemplate, @Value("${data.ingestion.service.url}") String dataIngestionServiceUrl) {
        this.restTemplate = restTemplate;
        this.dataIngestionServiceUrl = dataIngestionServiceUrl;
    }

    public List<Price> getPrices(String symbols, LocalDateTime startDate, LocalDateTime endDate) {
        String url = dataIngestionServiceUrl + "/api/prices";
        if (symbols != null && !symbols.isEmpty()) {
            url += "?symbols=" + symbols;
        }

        try {
            Price[] prices = restTemplate.getForObject(url, Price[].class);
            if (prices == null || prices.length == 0) {
                return List.of();
            }

            List<Price> priceList = Arrays.asList(prices);

            if (startDate != null && endDate != null) {
                return priceList.stream()
                        .filter(price -> price.getTimestamp().isAfter(startDate) && price.getTimestamp().isBefore(endDate))
                        .toList();
            }

            return priceList;

        } catch (Exception e) {
            logger.error("Error fetching prices from data ingestion service: {}", e.getMessage());
            return List.of();
        }
    }
}