package com.jwick.continental.deathagreement.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ContinentalConfig {
    @Value("${spring.datasource.driver-class-name}")
    private String dbDriverClassName;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${continental.btc-address}")
    private String continentalBtcAddress;

    @Value("${continental.unit}")
    private String continentalUnit;

    @Value("${continental.purge-pending-bet-in-days}")
    private long purgePendingBetInDays;

    @Value("${continental.maximum-bets-in-month}")
    private long maximumBetsInMonth;
}
