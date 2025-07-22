package com.oopsw.foodservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodApiWrapperDto {
	private Response response;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Response {
		private Body body;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Body {
		private List<FoodApiDto> items;
		private String totalCount;
	}



}
