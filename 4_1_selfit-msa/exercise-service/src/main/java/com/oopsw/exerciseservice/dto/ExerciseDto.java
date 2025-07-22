package com.oopsw.exerciseservice.dto;

import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ExerciseDto {
	private Long id;
	private String memberId;
	private String exerciseId;
	private LocalDate exerciseDate;
	private int exerciseMin;
	private float exerciseKcal;
	private float exerciseSum;
	private float exerciseAvg;
	private String year;
	private int newMin;


	private int pageNo;
	private int numOfRows;
	private String keyword;

	@JsonProperty("운동명")
	private String exerciseName;
	@JsonProperty("단위체중당에너지소비량")
	private float met;

	private float weight;
}
