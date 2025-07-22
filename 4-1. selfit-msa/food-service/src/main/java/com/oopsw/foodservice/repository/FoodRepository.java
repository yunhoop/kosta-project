package com.oopsw.foodservice.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oopsw.foodservice.jpa.FoodEntity;

public interface FoodRepository extends JpaRepository<FoodEntity, Long> {
	int deleteByMemberIdAndFoodId(String memberId, String foodId);
	FoodEntity findByMemberIdAndFoodId(String memberId, String foodId);
	List<FoodEntity> findByMemberIdAndIntakeDate(String memberId, Date intakeDate);
	List<FoodEntity> findByMemberIdAndIntakeDateBetween(String memberId, Date startDate, Date endDate);
}
