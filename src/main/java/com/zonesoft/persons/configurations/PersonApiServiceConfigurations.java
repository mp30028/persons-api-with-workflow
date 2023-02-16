package com.zonesoft.persons.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class PersonApiServiceConfigurations {
	
	
    public PersonApiServiceConfigurations() {
		super();
	}

	@Value("${server.port}")
    private int serverPort;
    
	@Bean
    public int getServerPort() {
		return serverPort;
	}

	@Bean
	public WebFluxConfigurer corsConfigurer() {
		return new WebFluxConfigurer() {
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins(
						"http://localhost:8080",
						"http://localhost:3000",
						"http://192.168.1.60:8080",
						"http://192.168.1.60:3000",
						"http://localhost:1111",
						"http://65.109.4.15:1111")
				.allowedMethods("GET", "POST", "PUT", "DELETE");
			}
		};
	}
}
