package br.com.jcv.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class JcvEurekaserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(JcvEurekaserverApplication.class, args);
	}

}
