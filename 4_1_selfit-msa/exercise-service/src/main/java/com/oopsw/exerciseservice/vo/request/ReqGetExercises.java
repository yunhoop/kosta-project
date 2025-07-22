package com.oopsw.exerciseservice.vo.request;

import java.time.LocalDate;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReqGetExercises {
	private LocalDate exerciseDate;
}
