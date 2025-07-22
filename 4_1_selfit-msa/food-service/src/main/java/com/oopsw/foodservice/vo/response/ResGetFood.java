package com.oopsw.foodservice.vo.response;

import java.util.Date;

import lombok.Data;

@Data
public class ResGetFood {
	private String foodId;
	private Date intakeDate;
	private String foodName;
	private Float intake;
	private Float intakeKcal;
	private Integer unitKcal;
	private String memberId;
}
