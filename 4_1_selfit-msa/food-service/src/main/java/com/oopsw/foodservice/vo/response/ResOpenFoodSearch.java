package com.oopsw.foodservice.vo.response;

import lombok.Data;

@Data
public class ResOpenFoodSearch {
	private String foodNm; //식품명
	private String enerc; //단위당_칼로리
	private String foodSize; //식품총중량
}
