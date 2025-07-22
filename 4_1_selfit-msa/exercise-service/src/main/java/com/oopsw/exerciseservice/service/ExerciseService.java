package com.oopsw.exerciseservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oopsw.exerciseservice.dto.ExerciseDto;

import reactor.core.publisher.Mono;

public interface ExerciseService {
	String addExercise(ExerciseDto exerciseDto);
	List<ExerciseDto> getExercises(ExerciseDto exerciseDto);
	void removeExercise(ExerciseDto exerciseDto);
	void setExerciseMin(ExerciseDto exerciseDto);
	ExerciseDto getExerciseKcal(ExerciseDto exerciseDto);
	List<ExerciseDto> getYearExerciseKcal(ExerciseDto exerciseDto);
	List<ExerciseDto> getYearExerciseAvgAll(ExerciseDto exerciseDto);
	Mono<List<ExerciseDto>> getExerciseOpenSearch(ExerciseDto exerciseDto);
}
