package com.oopsw.exerciseservice.vo.response;

import java.time.LocalDate;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResGetYearExerciseKcal {
	private LocalDate exerciseDate;
	private float exerciseSum;
}
