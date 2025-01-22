
package com.cryptotracker.api_gateway_service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {
        String environment = System.getenv("ENVIRONMENT");
        String response = "<html><head><title>SimpleApp</title></head><body><h1>HELLO FROM API-Gateway SERVICE</h1>";
        if (StringUtils.hasText(environment)) {
            response += "\n\n<h2>Environment: " + environment + "</h2>";
        }
        response += "</body></html>";
        return response;
    }
    @RequestMapping("/healthz")
    @ResponseStatus(HttpStatus.OK)
    public String health() {
        return "";
    }
    
}
