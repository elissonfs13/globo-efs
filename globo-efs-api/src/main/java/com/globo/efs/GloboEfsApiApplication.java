package com.globo.efs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GloboEfsApiApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(GloboEfsApiApplication.class, args);
	}

}
