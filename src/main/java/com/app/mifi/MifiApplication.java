package com.app.mifi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MifiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MifiApplication.class, args);
	}

}
