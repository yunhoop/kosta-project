package com.oopsw.commentservie.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
	Page<CommentEntity> findByBoardId(String boardId, Pageable pageable);
	
}
