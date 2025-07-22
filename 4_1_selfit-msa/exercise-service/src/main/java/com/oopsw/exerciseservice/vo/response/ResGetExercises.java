package com.oopsw.exerciseservice.vo.response;

import java.time.LocalDate;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResGetExercises {
	private String exerciseId;
	private LocalDate exerciseDate;
	private String exerciseName;
	private int exerciseMin;
	private float exerciseKcal;
}
