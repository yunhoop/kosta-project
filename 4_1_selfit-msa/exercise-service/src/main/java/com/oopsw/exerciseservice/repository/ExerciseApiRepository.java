package com.oopsw.exerciseservice.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import com.oopsw.exerciseservice.dto.ExerciseApiWrapper;
import com.oopsw.exerciseservice.dto.ExerciseDto;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Repository
public class ExerciseApiRepository {
	private final WebClient exerciseApiClient;
	@Value("Gngvp3UKvhulX6Jks8LsIebA534zldKcl1z7nB5IY7GLCTi91UO4BmwWutMo0zLFPMNFltzHI7Ab3uL1IoVt/g==")
	private String apiKey;


	public ExerciseApiRepository(WebClient exerciseApiClient) {
		this.exerciseApiClient = exerciseApiClient;
	}


	public Mono<List<ExerciseDto>> fetchExerciseData(int page, int size) {
		return exerciseApiClient.get()
			.uri(b -> b
				.queryParam("serviceKey", apiKey)
				.queryParam("page", page)
				.queryParam("perPage", size)
				.queryParam("type", "json")
				.build()
			)
			.retrieve()
			.bodyToMono(ExerciseApiWrapper.class)
			.map(ExerciseApiWrapper::getData);
	}
}
