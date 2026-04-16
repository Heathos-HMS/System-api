package com.backend.heathos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HeathosApplication {

	public static void main(String[] args) {
		SpringApplication.run(HeathosApplication.class, args);
	}

}
