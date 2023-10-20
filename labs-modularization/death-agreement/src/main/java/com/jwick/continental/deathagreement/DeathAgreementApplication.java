package com.jwick.continental.deathagreement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.jwick.continental.deathagreement",
		"br.com.jcv.commons.library",
		"br.com.jcv.codegen.codegenerator"
})
public class DeathAgreementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeathAgreementApplication.class, args);
	}

}
