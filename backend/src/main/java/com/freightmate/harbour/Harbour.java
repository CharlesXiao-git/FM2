package com.freightmate.harbour;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.freightmate.harbour")
public class Harbour {
	public static void main(String[] args) {
		SpringApplication.run(Harbour.class, args);
	}
}
