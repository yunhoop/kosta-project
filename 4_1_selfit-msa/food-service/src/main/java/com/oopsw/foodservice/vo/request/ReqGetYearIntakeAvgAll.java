package com.oopsw.foodservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReqGetYearIntakeAvgAll {
	private int year;
}
