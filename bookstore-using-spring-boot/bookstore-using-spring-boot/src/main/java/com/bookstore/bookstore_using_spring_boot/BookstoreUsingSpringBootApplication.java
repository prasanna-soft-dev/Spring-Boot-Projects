package com.bookstore.bookstore_using_spring_boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.bookstore.bookstore_using_spring_boot.entity")
public class BookstoreUsingSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookstoreUsingSpringBootApplication.class, args);
	}

}
