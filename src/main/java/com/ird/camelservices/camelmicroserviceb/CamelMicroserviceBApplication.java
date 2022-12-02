package com.ird.camelservices.camelmicroserviceb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.ird.camelservices.camelmicroserviceb.beans")
public class CamelMicroserviceBApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamelMicroserviceBApplication.class, args);
	}

}
