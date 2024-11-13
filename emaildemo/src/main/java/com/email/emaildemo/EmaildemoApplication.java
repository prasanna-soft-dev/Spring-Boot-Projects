package com.email.emaildemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.email")
public class EmaildemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmaildemoApplication.class, args);
	}

}
