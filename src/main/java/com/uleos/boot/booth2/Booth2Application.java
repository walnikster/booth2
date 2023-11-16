package com.uleos.boot.booth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class Booth2Application {
	public static void main(String[] args) {
		SpringApplication.run(Booth2Application.class, args);
	}
}
