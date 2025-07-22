package com.oopsw.exerciseservice.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oopsw.exerciseservice.jpa.ExerciseEntity;

public interface ExerciseRepository extends JpaRepository<ExerciseEntity, Long> {
	List<ExerciseEntity> findByExerciseDate(LocalDate exerciseDate);

	List<ExerciseEntity> findByExerciseDateAndMemberId(LocalDate exerciseDate, String memberId);
	boolean existsByMemberIdAndExerciseId(String memberId, String exerciseId);
	Integer deleteByMemberIdAndExerciseId(String memberId, String exerciseId);
	ExerciseEntity findByExerciseId(String exerciseId);
	List<ExerciseEntity> findByMemberIdAndExerciseDateBetween(String memberId, LocalDate startDate, LocalDate endDate);
	ExerciseEntity findByMemberIdAndExerciseId(String memberId, String exerciseId);
}
