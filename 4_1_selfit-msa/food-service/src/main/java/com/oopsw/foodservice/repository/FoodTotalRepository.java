package com.oopsw.foodservice.repository;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oopsw.foodservice.jpa.FoodTotalEntity;

public interface FoodTotalRepository extends JpaRepository<FoodTotalEntity, Long> {
	boolean existsByMemberIdAndIntakeDate(String memberId, LocalDate intakeDate);
	FoodTotalEntity findByMemberIdAndIntakeDate(String memberId, LocalDate intakeDate);

}
