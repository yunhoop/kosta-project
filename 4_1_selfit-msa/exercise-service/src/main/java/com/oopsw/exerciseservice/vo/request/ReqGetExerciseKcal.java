package com.oopsw.exerciseservice.vo.request;

import java.time.LocalDate;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReqGetExerciseKcal {
	private LocalDate exerciseDate;
}
