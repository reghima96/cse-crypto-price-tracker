package com.cryptotracker.price_data_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cryptotracker.price_data_service.repository.PriceEntity;
import com.cryptotracker.price_data_service.repository.PriceEntityRepository;

import jakarta.transaction.Transactional;

@Service
public class PriceService {

  private final PriceEntityRepository priceRepository;

  public PriceService(PriceEntityRepository priceRepository) {
    this.priceRepository = priceRepository;
  }

  public Page<PriceEntity> getAllPrices(Pageable pageable) {
    return priceRepository.findAll(pageable);
  }

  public Page<PriceEntity> getPricesBySymbols(List<String> symbols, Pageable pageable) {
    return priceRepository.findBySymbolIn(symbols, pageable);
  }

  public Page<PriceEntity> getHistoricalPrices(List<String> symbols, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
    return priceRepository.findPricesBySymbolsAndDateRange(symbols, startDate, endDate ,pageable);
  }

  public List<PriceEntity> getLatestPrices() {
    return priceRepository.findLatestPrices();
  }


  @Transactional
  public void save(PriceEntity price) {
    priceRepository.save(price);
  }
}