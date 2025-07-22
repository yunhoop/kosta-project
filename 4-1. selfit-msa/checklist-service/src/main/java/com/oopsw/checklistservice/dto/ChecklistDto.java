package com.oopsw.checklistservice.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChecklistDto {
	private Long id;
	private String memberId;
	private String checklistId;
	private String checklistContent;
	private Integer isChecked;
	private Date checklistDate;

}
