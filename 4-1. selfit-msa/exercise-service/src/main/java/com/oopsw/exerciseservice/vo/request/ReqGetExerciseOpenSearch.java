package com.oopsw.exerciseservice.vo.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReqGetExerciseOpenSearch {
	private String keyword;
	private int pageNo;
	private int numOfRows;
}
