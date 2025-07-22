package com.oopsw.exerciseservice.vo.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReqGetYearExerciseKcal {
	private String year;
}
