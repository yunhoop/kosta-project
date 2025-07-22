package com.oopsw.foodservice.dto;

import lombok.Data;

@Data
public class FoodApiDto {
	private String foodCd; //식품코드
	private String foodNm; //식품명
	private String enerc; //단위당_칼로리
	private String foodSize; //식품총중량

	private String keyword;
	private int pageNo;
	private int numOfRows;
}
