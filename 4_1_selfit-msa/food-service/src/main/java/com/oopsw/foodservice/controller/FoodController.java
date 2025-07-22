package com.oopsw.foodservice.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oopsw.foodservice.dto.FoodApiDto;
import com.oopsw.foodservice.dto.FoodDto;
import com.oopsw.foodservice.service.FoodService;
import com.oopsw.foodservice.vo.request.ReqAddFood;
import com.oopsw.foodservice.vo.request.ReqGetFood;
import com.oopsw.foodservice.vo.request.ReqGetIntakeKcal;
import com.oopsw.foodservice.vo.request.ReqGetYearIntakeAvgAll;
import com.oopsw.foodservice.vo.request.ReqGetYearIntakeKcal;
import com.oopsw.foodservice.vo.request.ReqOpenFoodSearch;
import com.oopsw.foodservice.vo.request.ReqRemoveFood;
import com.oopsw.foodservice.vo.request.ReqSetFood;
import com.oopsw.foodservice.vo.response.ResGetFood;
import com.oopsw.foodservice.vo.response.ResGetIntakeKcal;
import com.oopsw.foodservice.vo.response.ResGetYearIntakeAvgAll;
import com.oopsw.foodservice.vo.response.ResGetYearIntakeKcal;
import com.oopsw.foodservice.vo.response.ResMessage;
import com.oopsw.foodservice.vo.response.ResOpenFoodSearch;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/food-service")
public class FoodController {
	private final FoodService foodService;
	private final ModelMapper modelMapper;

	@GetMapping("/api-test")
	public String test() {
		return "test";
	}

	@PostMapping("/kcal/member/{memberId}")
	public ResponseEntity<ResGetIntakeKcal> getIntakeKcal(@PathVariable String memberId, @RequestBody ReqGetIntakeKcal reqGetIntakeKcal) {
		FoodDto foodDto = modelMapper.map(reqGetIntakeKcal, FoodDto.class);
		foodDto.setMemberId(memberId);
		ResGetIntakeKcal result = modelMapper.map(foodService.getIntakeKcal(foodDto), ResGetIntakeKcal.class);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/kcal/year/member/{memberId}")
	public ResponseEntity<List<ResGetYearIntakeKcal>> getYearIntakeKcal(@PathVariable String memberId, @RequestBody ReqGetYearIntakeKcal reqGetYearIntakeKcal) {
		FoodDto foodDto = modelMapper.map(reqGetYearIntakeKcal, FoodDto.class);
		foodDto.setMemberId(memberId);

		List<FoodDto> dtoList = foodService.getYearIntakeKcal(foodDto);

		List<ResGetYearIntakeKcal> result = new ArrayList<>();
		for (FoodDto dto : dtoList) {
			result.add(modelMapper.map(dto, ResGetYearIntakeKcal.class));
		}
		return ResponseEntity.ok(result);
	}

	@PostMapping("/avg/year/member/{memberId}")
	public ResponseEntity<List<ResGetYearIntakeAvgAll>> getYearIntakeAvgAll(@RequestBody ReqGetYearIntakeAvgAll reqGetYearIntakeAvgAll, @PathVariable String memberId) {
		FoodDto foodDto = FoodDto.builder()
			.memberId(memberId)
			.year(reqGetYearIntakeAvgAll.getYear())
			.build();

		List<FoodDto> avgPerDate = foodService.getYearIntakeAvgAll(foodDto);

		List<ResGetYearIntakeAvgAll> responseList = avgPerDate.stream()
			.map(dto -> ResGetYearIntakeAvgAll.builder()
				.intakeDate(dto.getIntakeDateLocal().toString())
				.avgIntakeKcal(dto.getIntakeAvg())
				.build())
			.toList();

		return ResponseEntity.ok(responseList);
	}

	@PostMapping("/foods/member/{memberId}")
	public ResponseEntity<List<ResGetFood>> getFood(@PathVariable("memberId") String memberId, @RequestBody ReqGetFood reqGetFood) {
		FoodDto foodDto = modelMapper.map(reqGetFood, FoodDto.class);
		foodDto.setMemberId(memberId);

		List<FoodDto> dtoList = foodService.getFood(foodDto);

		List<ResGetFood> result = new ArrayList<>();
		for (FoodDto dto : dtoList) {
			result.add(modelMapper.map(dto, ResGetFood.class));
		}
		return ResponseEntity.ok(result);

	}

	@PostMapping("/member/{memberId}")
	public ResponseEntity<ResMessage> addFood(@PathVariable("memberId") String memberId, @RequestBody ReqAddFood reqAddFood) {
		FoodDto foodDto = modelMapper.map(reqAddFood, FoodDto.class);
		foodDto.setMemberId(memberId);
		foodService.addFood(foodDto);
		return ResponseEntity.ok(new ResMessage("success"));
	}

	@PutMapping("/member/{memberId}")
	public ResponseEntity<ResMessage> setFood(@PathVariable("memberId") String memberId,@RequestBody ReqSetFood reqSetFood) {
		FoodDto foodDto = modelMapper.map(reqSetFood, FoodDto.class);
		foodDto.setMemberId(memberId);
		foodService.setFood(foodDto);
		return ResponseEntity.ok(new ResMessage("success"));
	}

	@DeleteMapping("/member/{memberId}")
	public ResponseEntity<ResMessage> removeFood(@PathVariable("memberId") String memberId, @RequestBody ReqRemoveFood reqRemoveFood) {
		FoodDto foodDto = modelMapper.map(reqRemoveFood, FoodDto.class);
		foodDto.setMemberId(memberId);
		foodService.removeFood(foodDto);
		return ResponseEntity.ok(new ResMessage("success"));
	}

	@PostMapping("open-search")
	public ResponseEntity<Mono<List<ResOpenFoodSearch>>> openFoodSearch(@RequestBody ReqOpenFoodSearch reqOpenFoodSearch) {
		FoodApiDto foodApiDto = modelMapper.map(reqOpenFoodSearch, FoodApiDto.class);

		Mono<List<ResOpenFoodSearch>> body = foodService.getFoodByNameLike(foodApiDto)
			.map(dtoList -> {
				List<ResOpenFoodSearch> result = new ArrayList<>();
				for (FoodApiDto dto : dtoList) {
					result.add(modelMapper.map(dto, ResOpenFoodSearch.class));
				}
				return result;
			});
		return ResponseEntity.ok(body);
	}
}
