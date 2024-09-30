package com.example.thehiveapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.thehiveapp.mapper")
public class TheHiveAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(TheHiveAppApplication.class, args);
	}

}
