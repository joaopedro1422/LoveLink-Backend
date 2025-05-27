package com.lovelink.LoveLink_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class LoveLinkBackendApplication {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(LoveLinkBackendApplication.class);

		Map<String, Object> props = new HashMap<>();
		String port = System.getenv("PORT");
		if (port != null) {
			props.put("server.port", port); // Usa a porta que o Railway define
		}

		app.setDefaultProperties(props);
		app.run(args);
	}
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
//add
}
