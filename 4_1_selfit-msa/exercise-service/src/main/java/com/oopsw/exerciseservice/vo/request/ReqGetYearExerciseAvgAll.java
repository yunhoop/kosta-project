package com.oopsw.exerciseservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqGetYearExerciseAvgAll {
	private String year;
}
