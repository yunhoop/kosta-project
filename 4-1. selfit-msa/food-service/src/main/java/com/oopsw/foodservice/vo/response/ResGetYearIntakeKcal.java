package com.oopsw.foodservice.vo.response;

import java.util.Date;

import lombok.Data;

@Data
public class ResGetYearIntakeKcal {
	private Date intakeDate;
	private Float intakeKcalSum;
}
