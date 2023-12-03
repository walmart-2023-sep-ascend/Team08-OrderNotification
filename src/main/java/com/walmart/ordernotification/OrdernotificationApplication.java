package com.walmart.ordernotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "com.walmart.*")
@Configuration
@EnableScheduling
public class OrdernotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdernotificationApplication.class, args);
	}

}
