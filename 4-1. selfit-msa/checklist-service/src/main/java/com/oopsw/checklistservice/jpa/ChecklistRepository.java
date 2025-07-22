package com.oopsw.checklistservice.jpa;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistRepository extends JpaRepository<ChecklistEntity, Long> {
	List<ChecklistEntity> findAllByMemberIdAndChecklistDate(String memberId, Date checklistDate);

	Optional<ChecklistEntity> findByChecklistIdAndMemberId(String checklistId, String memberId);

}
