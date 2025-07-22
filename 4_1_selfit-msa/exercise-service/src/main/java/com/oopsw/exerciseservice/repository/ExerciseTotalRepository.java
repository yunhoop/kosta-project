package com.oopsw.exerciseservice.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oopsw.exerciseservice.jpa.ExerciseTotalEntity;

public interface ExerciseTotalRepository extends JpaRepository<ExerciseTotalEntity, Long> {

	boolean existsByMemberIdAndExerciseDate(String memberId, LocalDate exerciseDate);
	ExerciseTotalEntity findByMemberIdAndExerciseDate(String memberId, LocalDate exerciseDate);
	ExerciseTotalEntity findByExerciseDateAndMemberId(LocalDate exerciseDate, String memberId);
}
