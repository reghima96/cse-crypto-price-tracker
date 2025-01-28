package com.cryptotracker.price_data_service.service;

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

  @Transactional
  public void save(PriceEntity price) {
    priceRepository.save(price);
  }
}