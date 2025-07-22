package com.oopsw.foodservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResGetYearIntakeAvgAll {
	private String intakeDate;
	private float avgIntakeKcal;
}
