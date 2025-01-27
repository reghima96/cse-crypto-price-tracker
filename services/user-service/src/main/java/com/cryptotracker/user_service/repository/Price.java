package com.cryptotracker.user_service.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Price {
  @Id
  private UUID id;
  private String cryptocurrency;
  private String symbol;
  private BigDecimal price;
  private LocalDateTime timestamp;
}
