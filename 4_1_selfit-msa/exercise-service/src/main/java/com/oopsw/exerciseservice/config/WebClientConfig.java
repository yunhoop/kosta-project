package com.oopsw.exerciseservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Value("https://api.odcloud.kr/api/15068730/v1/uddi:e57a5dba-bbbf-414e-a5cd-866c48378daa")
	private String exerciseBaseUrl;

	@Bean("exerciseApiClient")
	public WebClient exerciseApiClient(WebClient.Builder builder) {
		return builder
			.baseUrl(exerciseBaseUrl)
			.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
			.build();
	}
}
