package com.cryptotracker.price_data_service.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cryptotracker.price_data_service.dto.CryptocurrencyDto;
import com.cryptotracker.price_data_service.repository.Cryptocurrency;
import com.cryptotracker.price_data_service.repository.PriceEntity;
import com.cryptotracker.price_data_service.service.PriceService;

@Controller
@RequestMapping("/api/prices")
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping("/{symbol}/recent")
    @ResponseBody
    public ResponseEntity<List<PriceEntity>> getRecentPrices(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "24h") String timeRange) {
        List<PriceEntity> prices = priceService.getRecentPricesBySymbol(symbol, timeRange);
        return ResponseEntity.ok(prices);
    }

    @GetMapping("/dashboard")
    public String getDashboard(
            @RequestHeader(value = "X-User-Email", required = false) String userEmail,
            Model model) {
        if (userEmail == null) {
            return "redirect:/auth/login";
        }
        
        List<Cryptocurrency> cryptocurrencies = priceService.getAllCryptocurrencies();
        model.addAttribute("cryptocurrencies", cryptocurrencies);
        model.addAttribute("userEmail", userEmail);
        return "dashboard";
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData(
            @RequestParam String timeRange) throws IOException {
        byte[] excelContent = priceService.generateExcelReport(timeRange);

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "crypto_prices_" + timestamp + ".xlsx";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", filename);

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelContent);
    }

    @PostMapping("/admin/cryptocurrency")
    @ResponseBody
    public ResponseEntity<?> addCryptocurrency(
            @RequestHeader("X-User-Roles") String roles,
            @RequestBody CryptocurrencyDto dto) {

        if (!roles.contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Admin access required");
        }

        try {
            Cryptocurrency crypto = priceService.addCryptocurrency(dto);
            return ResponseEntity.ok(crypto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
