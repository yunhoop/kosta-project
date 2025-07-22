package com.oopsw.foodservice.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import com.oopsw.foodservice.dto.FoodApiDto;
import com.oopsw.foodservice.dto.FoodApiWrapperDto;

import reactor.core.publisher.Mono;

@Repository
public class FoodApiRepository {
	private final WebClient foodApiClient;
	@Value("${food.key}")
	private String apiKey;

	public FoodApiRepository(WebClient foodApiClient) {
		this.foodApiClient = foodApiClient;
	}

	/** 외부 API에서 하루치 데이터를 가져와 List<FoodApi>로 반환 */
	public Mono<List<FoodApiDto>> fetchFoodData(int pageNo, int numOfRows) {
		return foodApiClient.get()
			.uri(uri -> uri
				.queryParam("serviceKey", apiKey)
				.queryParam("pageNo", pageNo)
				.queryParam("numOfRows", numOfRows)
				.queryParam("type", "json")
				.build()
			)
			.retrieve()
			.bodyToMono(FoodApiWrapperDto.class)
			.map(wrapper -> wrapper.getResponse().getBody().getItems());
	}

}
