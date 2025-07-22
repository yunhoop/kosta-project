package com.oopsw.foodservice.dto;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodDto {
	private Long id;
	private String foodId;
	private Date intakeDate;
	private String foodName;
	private Float intake;
	private Float intakeKcal;
	private Float intakeAvg;
	private Integer unitKcal;
	private String memberId;
	private LocalDate intakeDateLocal;
	private Float intakeKcalSum;
	private int year;
}
