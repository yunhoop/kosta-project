package com.oopsw.checklistservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.oopsw.checklistservice.dto.ChecklistDto;
import com.oopsw.checklistservice.jpa.ChecklistEntity;
import com.oopsw.checklistservice.jpa.ChecklistRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChecklistServiceImpl implements ChecklistService {
	private final ChecklistRepository checklistRepository;
	private final ModelMapper modelMapper;

	@Override
	public void setCheckItem(ChecklistDto checklistDto) {
		Optional<ChecklistEntity> optionalEntity = checklistRepository
			.findByChecklistIdAndMemberId(checklistDto.getChecklistId(), checklistDto.getMemberId());

		if (optionalEntity.isPresent()) {
			ChecklistEntity entity = optionalEntity.get();
			entity.setChecklistContent(checklistDto.getChecklistContent());
			checklistRepository.save(entity);
		} else {
			throw new IllegalArgumentException("해당 체크리스트 항목을 찾을 수 없습니다.");
		}
	}

	@Override
	public List<ChecklistDto> getChecklists(ChecklistDto checklistDto) {
		System.out.println(checklistDto.getChecklistDate());
		List<ChecklistEntity> entities = checklistRepository
			.findAllByMemberIdAndChecklistDate(checklistDto.getMemberId(), checklistDto.getChecklistDate());

		List<ChecklistDto> result = new ArrayList<>();
		for (ChecklistEntity entity : entities) {
			ChecklistDto dto = modelMapper.map(entity, ChecklistDto.class);
			result.add(dto);
		}

		return result;
	}

	@Override
	public void setIsCheckItem(ChecklistDto checklistDto) {
		Optional<ChecklistEntity> optionalEntity = checklistRepository
			.findByChecklistIdAndMemberId(checklistDto.getChecklistId(), checklistDto.getMemberId());

		if (optionalEntity.isPresent()) {
			ChecklistEntity entity = optionalEntity.get();
			entity.setIsChecked(checklistDto.getIsChecked());
			checklistRepository.save(entity);
		} else {
			throw new IllegalArgumentException("해당 체크리스트 항목이 존재하지 않습니다.");
		}

	}

	@Override
	public void removeChecklist(ChecklistDto checklistDto) {
		Optional<ChecklistEntity> checklistEntity = checklistRepository.findByChecklistIdAndMemberId(
			checklistDto.getChecklistId(), checklistDto.getMemberId());
		if (checklistEntity.isPresent()) {
			checklistRepository.delete(checklistEntity.get());
		} else {
			throw new IllegalArgumentException("해당 체크리스트 항목이 존재하지 않습니다.");
		}

	}

	@Override
	public String addChecklist(ChecklistDto checklistDto) {
		// int count = (checklistRepository.findAll().size()) + 1;
		// checklistDto.setChecklistId(String.format("Ch%03d", count));
		checklistDto.setChecklistId(UUID.randomUUID().toString());
		checklistRepository.save(modelMapper.map(checklistDto, ChecklistEntity.class));
		return checklistDto.getChecklistId();
	}
}

