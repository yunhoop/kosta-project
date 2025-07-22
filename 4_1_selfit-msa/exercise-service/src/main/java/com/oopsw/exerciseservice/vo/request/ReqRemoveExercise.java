package com.oopsw.exerciseservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
public class ReqRemoveExercise {
	private String exerciseId;
}
