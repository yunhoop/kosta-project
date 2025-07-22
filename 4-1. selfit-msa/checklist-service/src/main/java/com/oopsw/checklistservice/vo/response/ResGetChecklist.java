package com.oopsw.checklistservice.vo.response;

import java.util.Date;

import lombok.Data;

@Data
public class ResGetChecklist {
	private String checklistId;
	private String checklistContent;
	private Integer isChecked;
	private Date checklistDate;
}
