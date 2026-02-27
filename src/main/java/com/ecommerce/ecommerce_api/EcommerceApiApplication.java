package com.ecommerce.ecommerce_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = { "com.ecommerce.ecommerce_api", "com.murgan.ecommerce" })
@EntityScan(basePackages = { "com.murgan.ecommerce.domain" })
@EnableJpaRepositories(basePackages = { "com.murgan.ecommerce.repository" })
public class EcommerceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApiApplication.class, args);
	}

}
