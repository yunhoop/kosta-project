package com.oopsw.exerciseservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExerciseApiWrapper {
	private int currentCount;
	@JsonProperty("data")
	private List<ExerciseDto> data;
	private int matchCount;
	private int page;
	private int perPage;
	private int totalCount;
}
