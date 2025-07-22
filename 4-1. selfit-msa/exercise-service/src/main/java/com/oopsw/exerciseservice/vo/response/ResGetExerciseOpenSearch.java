package com.oopsw.exerciseservice.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResGetExerciseOpenSearch {
	@JsonProperty("단위체중당에너지소비량")
	private float met;


	@JsonProperty("운동명")
	private String exerciseName;
}
