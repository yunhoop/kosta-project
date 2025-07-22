package com.oopsw.foodservice.vo.request;

import lombok.Data;

@Data
public class ReqOpenFoodSearch {
	private String keyword;
	private int pageNo;
	private int numOfRows;
}
