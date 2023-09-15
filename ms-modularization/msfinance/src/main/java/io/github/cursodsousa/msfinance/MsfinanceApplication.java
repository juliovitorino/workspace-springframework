package io.github.cursodsousa.msfinance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsfinanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsfinanceApplication.class, args);
	}

}
