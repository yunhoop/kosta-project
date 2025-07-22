package com.oopsw.checklistservice.vo.request;

import java.util.Date;

import lombok.Data;

@Data
public class ReqAddCheckItem {
	private Integer checklistId;
	private String checklistContent;
	private Integer isChecked;
	private Date checklistDate;
}
