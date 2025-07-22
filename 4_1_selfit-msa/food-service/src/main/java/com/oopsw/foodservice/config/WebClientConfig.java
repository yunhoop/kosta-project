package com.oopsw.foodservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
	@Value("${food.base-url}")
	private String foodBaseUrl;

	@Bean("foodApiClient")
	public WebClient foodApiClient(WebClient.Builder builder) {
		return builder
			.baseUrl(foodBaseUrl)
			.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
			.build();
	}
}
