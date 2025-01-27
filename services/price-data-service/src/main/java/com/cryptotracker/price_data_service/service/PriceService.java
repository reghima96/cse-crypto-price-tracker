package com.cryptotracker.price_data_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cryptotracker.price_data_service.repository.PriceEntity;
import com.cryptotracker.price_data_service.repository.PriceEntityRepository;

@Service
public class PriceService {

  private final PriceEntityRepository priceRepository;

  public PriceService(PriceEntityRepository priceRepository) {
    this.priceRepository = priceRepository;
  }

  public List<PriceEntity> getAllPrices() {
    return priceRepository.findAll();
  }

  public List<PriceEntity> getPricesBySymbols(List<String> symbols) {
    return priceRepository.findBySymbolIn(symbols);
  }

  public void save(PriceEntity price) {
    priceRepository.save(price);
  }
}