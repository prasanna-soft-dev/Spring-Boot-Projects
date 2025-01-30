package com.jwt.token;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.jwt.token.Entity")
public class TokenApplication {

	public static void main(String[] args) {

		SpringApplication.run(TokenApplication.class, args);
	}

}
