package com.cryptotracker.price_data_service.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.cryptotracker.price_data_service.dto.CryptocurrencyDto;
import com.cryptotracker.price_data_service.repository.Cryptocurrency;
import com.cryptotracker.price_data_service.repository.CryptocurrencyRepository;
import com.cryptotracker.price_data_service.repository.PriceEntity;
import com.cryptotracker.price_data_service.repository.PriceEntityRepository;

import jakarta.transaction.Transactional;

@Service
public class PriceService {
  private final PriceEntityRepository priceEntityRepository;
  private final CryptocurrencyRepository cryptocurrencyRepository;

  public PriceService(PriceEntityRepository priceEntityRepository,
      CryptocurrencyRepository cryptocurrencyRepository) {
    this.priceEntityRepository = priceEntityRepository;
    this.cryptocurrencyRepository = cryptocurrencyRepository;
  }

  @Transactional
  public void save(PriceEntity price) {
    priceEntityRepository.save(price);
  }

  public List<PriceEntity> getRecentPricesBySymbol(String symbol, String timeRange) {
    LocalDateTime since = calculateTimeRange(timeRange);
    return priceEntityRepository.findRecentPricesBySymbolAndTimeRange(symbol, since);
  }

  public List<Cryptocurrency> getAllCryptocurrencies() {
    return cryptocurrencyRepository.findAll();
  }

  public byte[] generateExcelReport(String timeRange) throws IOException {
    LocalDateTime since = calculateTimeRange(timeRange);
    List<PriceEntity> prices = priceEntityRepository.findByTimestampGreaterThanEqual(since);

    try (Workbook workbook = new XSSFWorkbook()) {
      Sheet sheet = workbook.createSheet("Crypto Prices");

      // Create header row
      Row headerRow = sheet.createRow(0);
      headerRow.createCell(0).setCellValue("Symbol");
      headerRow.createCell(1).setCellValue("Price (EUR)");
      headerRow.createCell(2).setCellValue("Timestamp");

      // Add data rows
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      int rowNum = 1;
      for (PriceEntity price : prices) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(price.getSymbol());
        row.createCell(1).setCellValue(price.getPrice().doubleValue());
        row.createCell(2).setCellValue(price.getTimestamp().format(formatter));
      }

      // Autosize columns
      for (int i = 0; i < 3; i++) {
        sheet.autoSizeColumn(i);
      }

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      workbook.write(outputStream);
      return outputStream.toByteArray();
    }
  }

  private LocalDateTime calculateTimeRange(String timeRange) {
    LocalDateTime now = LocalDateTime.now();
    return switch (timeRange) {
    case "1h" -> now.minusHours(1);
    case "3d" -> now.minusDays(3);
    default -> now.minusHours(24); // 24h is default
    };
  }

  public Cryptocurrency addCryptocurrency(CryptocurrencyDto dto) {
    if (cryptocurrencyRepository.existsBySymbol(dto.getSymbol())) {
      throw new IllegalArgumentException("Cryptocurrency with this symbol already exists");
    }

    Cryptocurrency crypto = new Cryptocurrency();
    crypto.setSymbol(dto.getSymbol().toUpperCase());
    crypto.setCoinGeckoId(dto.getCoinGeckoId().toLowerCase());

    return cryptocurrencyRepository.save(crypto);
  }
}