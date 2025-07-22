package com.oopsw.exerciseservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResGetYearExerciseAvgAll {
	private String exerciseDate;
	private float avgExerciseKcal;
}
