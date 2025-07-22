package com.oopsw.checklistservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oopsw.checklistservice.dto.ChecklistDto;
import com.oopsw.checklistservice.service.ChecklistService;
import com.oopsw.checklistservice.vo.request.ReqAddCheckList;
import com.oopsw.checklistservice.vo.request.ReqGetChecklist;
import com.oopsw.checklistservice.vo.request.ReqRemoveChecklist;
import com.oopsw.checklistservice.vo.request.ReqSetCheckItem;
import com.oopsw.checklistservice.vo.request.ReqSetIsCheckItem;
import com.oopsw.checklistservice.vo.response.ResAddChecklist;
import com.oopsw.checklistservice.vo.response.ResGetChecklist;
import com.oopsw.checklistservice.vo.response.ResMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/checklist-service")
public class ChecklistController {
	private final ChecklistService checklistService;
	private final ModelMapper modelMapper;

	@GetMapping("/api-test")
	public String test() {
		return "test";
	}

	@PostMapping("/member/{memberId}")
	public ResponseEntity<List<ResGetChecklist>> getCheckList(@PathVariable String memberId,
		@RequestBody ReqGetChecklist reqGetChecklist) {
		ChecklistDto checklistDto = modelMapper.map(reqGetChecklist, ChecklistDto.class);
		checklistDto.setMemberId(memberId);
		List<ChecklistDto> checklistDtos = checklistService.getChecklists(checklistDto);
		List<ResGetChecklist> response = new ArrayList<>();
		for (ChecklistDto dto : checklistDtos) {
			ResGetChecklist res = modelMapper.map(dto, ResGetChecklist.class);
			response.add(res);
		}

		return ResponseEntity.ok(response);
	}

	@PutMapping("/member/{memberId}")
	public ResponseEntity<ResMessage> setCheckItem(@PathVariable String memberId,
		@RequestBody ReqSetCheckItem reqSetCheckItem) {
		ChecklistDto checklistDto = modelMapper.map(reqSetCheckItem, ChecklistDto.class);
		checklistDto.setMemberId(memberId);
		checklistService.setCheckItem(checklistDto);
		return ResponseEntity.ok(new ResMessage("success"));
	}

	@PutMapping("/item/checklist/member/{memberId}")
	public ResponseEntity<ResMessage> setIsCheckItem(@PathVariable String memberId,
		@RequestBody ReqSetIsCheckItem reqSetIsCheckItem) {
		ChecklistDto checklistDto = modelMapper.map(reqSetIsCheckItem, ChecklistDto.class);
		checklistDto.setMemberId(memberId);
		checklistService.setIsCheckItem(checklistDto);
		return ResponseEntity.ok(new ResMessage("success"));
	}

	@DeleteMapping("/item/checklist/member/{memberId}")
	public ResponseEntity<ResMessage> removeChecklist(@PathVariable String memberId,
		@RequestBody ReqRemoveChecklist reqRemoveChecklist) {
		ChecklistDto checklistDto = modelMapper.map(reqRemoveChecklist, ChecklistDto.class);
		checklistDto.setMemberId(memberId);
		checklistService.removeChecklist(checklistDto);
		return ResponseEntity.ok(new ResMessage("success"));
	}

	@PostMapping("/item/member/{memberId}")
	public ResponseEntity<ResAddChecklist> addChecklist(@PathVariable String memberId,
		@RequestBody ReqAddCheckList reqAddCheckList) {
		ChecklistDto checkListDto = modelMapper.map(reqAddCheckList, ChecklistDto.class);
		checkListDto.setMemberId(memberId);
		String checklistId = checklistService.addChecklist(checkListDto);
		return ResponseEntity.ok(new ResAddChecklist(checklistId));
	}

}
