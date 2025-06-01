package com.oopsw.selfit.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oopsw.selfit.domain.ExerciseInfos;
import com.oopsw.selfit.dto.Exercise;
import com.oopsw.selfit.repository.DashboardRepository;
import com.oopsw.selfit.repository.ExerciseInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExerciseInfoService {
	private final ExerciseInfoRepository exerciseInfoRepository;
	private final DashboardRepository dashboardRepository;

	public boolean removeExercise(int exerciseInfoId) {
		if (exerciseInfoRepository.existsById(exerciseInfoId)) {
			exerciseInfoRepository.deleteById(exerciseInfoId);
			return true;
		}
		return false;
	}

	public boolean addExerciseInfo(Exercise exercise) {
		// 소모 칼로리 계산
		float kcal = dashboardRepository.getWeight(exercise.getExerciseNoteId()) *
			exercise.getMet() *
			exercise.getExerciseMin() / 60f;

		// DTO → Entity 수동 변환
		ExerciseInfos entity = ExerciseInfos.builder()
			.exerciseNoteId(exercise.getExerciseNoteId())
			.exerciseMin(exercise.getExerciseMin())
			.exerciseKcal(kcal)
			.build();

		// JPA 저장
		exerciseInfoRepository.save(entity);
		return true;
	}




}



