package io.github.cursodsousa.msmarketing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsmarketingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsmarketingApplication.class, args);
	}

}
