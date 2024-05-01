package com.kh.woofly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class WooflyApplication {

	public static void main(String[] args) {
		SpringApplication.run(WooflyApplication.class, args);
	}

}
