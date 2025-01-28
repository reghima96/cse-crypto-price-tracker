package com.cryptotracker.user_service;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean
    public Flyway flyway() {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .baselineOnMigrate(true)
                .baselineVersion("1")
                .baselineDescription("create tables ")
                .load();
        flyway.migrate(); 
        return flyway;
    }
}