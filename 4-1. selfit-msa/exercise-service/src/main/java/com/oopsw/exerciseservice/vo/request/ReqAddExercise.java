package com.oopsw.exerciseservice.vo.request;

import java.time.LocalDate;
import java.util.Date;

import lombok.Data;

@Data
public class ReqAddExercise {
	private LocalDate exerciseDate;
	private String exerciseName;
	private int exerciseMin;
	private float met;
}
