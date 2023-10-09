package com.jwick.continental.deathagreement.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ContinentalConfig {

    @Value("${continental.btc-address}")
    private String continentalBtcAddress;

    @Value("${continental.unit}")
    private String continentalUnit;
}
