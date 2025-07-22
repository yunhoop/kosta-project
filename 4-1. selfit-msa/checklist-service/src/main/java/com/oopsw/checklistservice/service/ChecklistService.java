package com.oopsw.checklistservice.service;

import java.util.List;

import com.oopsw.checklistservice.dto.ChecklistDto;

public interface ChecklistService {

	List<ChecklistDto> getChecklists(ChecklistDto checklistDto);

	String addChecklist(ChecklistDto checklistDto);

	void removeChecklist(ChecklistDto checklistDto);

	void setIsCheckItem(ChecklistDto checklistDto);

	void setCheckItem(ChecklistDto checklistDto);

}
