package com.oopsw.checklistservice.vo.request;

import lombok.Data;

@Data
public class ReqSetIsCheckItem {
	private String checklistId;
	private int isChecked;

}
