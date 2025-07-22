package com.oopsw.exerciseservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.oopsw.exerciseservice.dto.ExerciseDto;
import com.oopsw.exerciseservice.repository.ExerciseRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class ExerciseServiceTests {
	@Autowired
	private ExerciseRepository exerciseRepository;

	@Autowired
	private ExerciseService exerciseService;

	// @Test
	// public void testAddExercise() {
	// 	ExerciseDto exerciseDto = ExerciseDto.builder()
	// 		.exerciseName("걷기")
	// 		.exerciseMin(30)
	// 		.exerciseDate("2025-01-01")
	// 		.memberId("m001")
	// 		.met(5.3f)
	// 		.build();
	// 	exerciseService.addExercise(exerciseDto);
	// 	log.info(exerciseDto);
	// }


}

