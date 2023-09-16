package io.github.cursodsousa.msadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsadminApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsadminApplication.class, args);
	}

}
