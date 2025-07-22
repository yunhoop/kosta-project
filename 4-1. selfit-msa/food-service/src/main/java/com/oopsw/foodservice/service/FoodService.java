package com.oopsw.foodservice.service;

import java.util.List;

import com.oopsw.foodservice.dto.FoodApiDto;
import com.oopsw.foodservice.dto.FoodDto;

import reactor.core.publisher.Mono;

public interface FoodService {
	FoodDto getIntakeKcal(FoodDto foodDto);
	List<FoodDto> getYearIntakeKcal(FoodDto foodDto);
	List<FoodDto> getFood(FoodDto foodDto);
	FoodDto addFood(FoodDto foodDto);
	FoodDto setFood(FoodDto foodDto);
	void removeFood(FoodDto foodDto);
	Mono<List<FoodApiDto>> getFoodByNameLike(FoodApiDto foodApiDto);
	List<FoodDto> getYearIntakeAvgAll(FoodDto foodDto);
}
