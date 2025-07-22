package com.oopsw.exerciseservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oopsw.exerciseservice.dto.ExerciseDto;
import com.oopsw.exerciseservice.service.ExerciseService;
import com.oopsw.exerciseservice.vo.request.ReqAddExercise;
import com.oopsw.exerciseservice.vo.request.ReqGetExerciseKcal;
import com.oopsw.exerciseservice.vo.request.ReqGetExerciseOpenSearch;
import com.oopsw.exerciseservice.vo.request.ReqGetExercises;
import com.oopsw.exerciseservice.vo.request.ReqGetYearExerciseAvgAll;
import com.oopsw.exerciseservice.vo.request.ReqGetYearExerciseKcal;
import com.oopsw.exerciseservice.vo.request.ReqRemoveExercise;
import com.oopsw.exerciseservice.vo.request.ReqSetExerciseMin;
import com.oopsw.exerciseservice.vo.response.ResAddExercise;
import com.oopsw.exerciseservice.vo.response.ResGetExerciseKcal;
import com.oopsw.exerciseservice.vo.response.ResGetExerciseOpenSearch;
import com.oopsw.exerciseservice.vo.response.ResGetExercises;
import com.oopsw.exerciseservice.vo.response.ResGetYearExerciseAvgAll;
import com.oopsw.exerciseservice.vo.response.ResGetYearExerciseKcal;
import com.oopsw.exerciseservice.vo.response.ResMessage;
import com.oopsw.exerciseservice.vo.response.ResAddExercise;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("api/exercise-service")
public class ExerciseController {
	private final ExerciseService exerciseService;

	@GetMapping("/api-test")
	public String test() {
		return "test";
	}

	@PostMapping("/member/{memberId}")
	public ResponseEntity<ResAddExercise> addExercise(@RequestBody ReqAddExercise reqAddExercise, @PathVariable String memberId) {
		ExerciseDto exerciseDto = ExerciseDto.builder()
			.exerciseDate(reqAddExercise.getExerciseDate())
			.exerciseMin(reqAddExercise.getExerciseMin())
			.met(reqAddExercise.getMet())
			.exerciseName(reqAddExercise.getExerciseName())
			.memberId(memberId).build();
		String exerciseId = exerciseService.addExercise(exerciseDto);
		return ResponseEntity.ok(new ResAddExercise(exerciseId));
	}

	@PostMapping("/exercises/member/{memberId}")
	public ResponseEntity<List<ResGetExercises>> getExercises(@RequestBody ReqGetExercises reqGetExercises, @PathVariable String memberId) {
		ExerciseDto exerciseDto = ExerciseDto.builder()
			.exerciseDate(reqGetExercises.getExerciseDate())
			.memberId(memberId)
			.build();
		List<ExerciseDto> exerciseDtos = exerciseService.getExercises(exerciseDto);
		List<ResGetExercises> resGetExercises = new ArrayList<>();
		for (ExerciseDto exerciseDto1 : exerciseDtos) {
			ResGetExercises resGetExercise = ResGetExercises.builder()
				.exerciseId(exerciseDto1.getExerciseId())
				.exerciseDate(exerciseDto1.getExerciseDate())
				.exerciseKcal(exerciseDto1.getExerciseKcal())
				.exerciseName(exerciseDto1.getExerciseName())
				.exerciseMin(exerciseDto1.getExerciseMin())
				.build();
			resGetExercises.add(resGetExercise);
		}
		return ResponseEntity.ok(resGetExercises);
	}

	@DeleteMapping("/member/{memberId}")
	public ResponseEntity<ResMessage> removeExercise(@RequestBody ReqRemoveExercise reqRemoveExercise, @PathVariable String memberId) {
		ExerciseDto exerciseDto = ExerciseDto.builder()
			.memberId(memberId)
			.exerciseId(reqRemoveExercise.getExerciseId())
			.build();
		exerciseService.removeExercise(exerciseDto);
		return ResponseEntity.ok(new ResMessage("success"));
	}

	@PutMapping("/member/{memberId}")
	public ResponseEntity<ResMessage> setExerciseMin(@RequestBody ReqSetExerciseMin reqSetExerciseMin, @PathVariable String memberId) {
		ExerciseDto exerciseDto = ExerciseDto.builder()
			.exerciseId(reqSetExerciseMin.getExerciseId())
			.memberId(memberId)
			.newMin(reqSetExerciseMin.getNewMin())
			.build();
		exerciseService.setExerciseMin(exerciseDto);
		return ResponseEntity.ok(new ResMessage("success"));
	}

	@PostMapping("/kcal/member/{memberId}")
	public ResponseEntity<ResGetExerciseKcal> getExerciseKcal(@RequestBody ReqGetExerciseKcal reqGetExerciseKcal, @PathVariable String memberId) {
		ExerciseDto exerciseDto = ExerciseDto.builder()
			.exerciseDate(reqGetExerciseKcal.getExerciseDate())
			.memberId(memberId)
			.build();
		ExerciseDto resultDto = exerciseService.getExerciseKcal(exerciseDto);
		ResGetExerciseKcal resGetExerciseKcal = ResGetExerciseKcal.builder()
			.exerciseSum(resultDto.getExerciseSum())
			.build();
		return ResponseEntity.ok(resGetExerciseKcal);
	}

	@PostMapping("/year/member/{memberId}")
	public ResponseEntity<List<ResGetYearExerciseKcal>> getYearExerciseKcal(@RequestBody ReqGetYearExerciseKcal reqGetYearExerciseKcal, @PathVariable String memberId) {
		ExerciseDto exerciseDto = ExerciseDto.builder()
			.memberId(memberId)
			.year(reqGetYearExerciseKcal.getYear())
			.build();

		List<ResGetYearExerciseKcal> resGetYearExerciseKcals = new ArrayList<>();
		List<ExerciseDto> exerciseDtos = exerciseService.getYearExerciseKcal(exerciseDto);
		for (ExerciseDto exerciseDto1 : exerciseDtos) {
			ResGetYearExerciseKcal resGetYearExerciseKcal = ResGetYearExerciseKcal.builder()
				.exerciseDate(exerciseDto1.getExerciseDate())
				.exerciseSum(exerciseDto1.getExerciseSum())
				.build();
			resGetYearExerciseKcals.add(resGetYearExerciseKcal);
		}
		return ResponseEntity.ok(resGetYearExerciseKcals);
	}

	@PostMapping("/avg/year/member/{memberId}")
	public ResponseEntity<List<ResGetYearExerciseAvgAll>> getYearExerciseAvgAll(@RequestBody ReqGetYearExerciseAvgAll reqGetYearExerciseAvgAll, @PathVariable String memberId) {
		ExerciseDto exerciseDto = ExerciseDto.builder()
			.memberId(memberId)
			.year(reqGetYearExerciseAvgAll.getYear())
			.build();

		List<ExerciseDto> avgPerDate = exerciseService.getYearExerciseAvgAll(exerciseDto);

		List<ResGetYearExerciseAvgAll> responseList = avgPerDate.stream()
			.map(dto -> ResGetYearExerciseAvgAll.builder()
				.exerciseDate(dto.getExerciseDate().toString())
				.avgExerciseKcal(dto.getExerciseAvg())
				.build())
			.toList();

		return ResponseEntity.ok(responseList);
	}

	@PostMapping("/open-search")
	public ResponseEntity<Mono<List<ResGetExerciseOpenSearch>>> getExerciseOpenSearch(@RequestBody ReqGetExerciseOpenSearch reqGetExerciseOpenSearch) {
		ExerciseDto exerciseDto = ExerciseDto.builder()
			.keyword(reqGetExerciseOpenSearch.getKeyword())
			.numOfRows(reqGetExerciseOpenSearch.getNumOfRows())
			.pageNo(reqGetExerciseOpenSearch.getPageNo())
			.build();

		Mono<List<ExerciseDto>> exerciseDtos = exerciseService.getExerciseOpenSearch(exerciseDto);

		Mono<List<ResGetExerciseOpenSearch>> resGetExerciseOpenSearch = exerciseDtos.map(dtoList ->
			dtoList.stream()
				.map(dto -> new ResGetExerciseOpenSearch(
					dto.getMet(), dto.getExerciseName()
					// 필요 시 다른 필드도 매핑
				))
				.collect(Collectors.toList())
		);
		return ResponseEntity.ok(resGetExerciseOpenSearch);
	}


}
