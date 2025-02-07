
package com.cryptotracker.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {
        String environment = System.getenv("env");
        String response = "<html><head><title>SimpleApp</title></head><body><h1>HELLO FROM USER  SERVICE</h1>";
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
