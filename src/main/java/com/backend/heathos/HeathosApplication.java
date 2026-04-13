package com.backend.heathos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class HeathosApplication {

	public static void main(String[] args) {
		SpringApplication.run(HeathosApplication.class, args);
	
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
        SpringApplication.run(HeathosApplication.class, args);
    }
}
