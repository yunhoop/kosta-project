package com.oopsw.boardservice.jpa;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.oopsw.boardservice.dto.BoardDto;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
	Optional<BoardEntity> findTopByOrderByBoardIdDesc();

	Page<BoardEntity> findByCategoryNameAndBoardTitleContainingIgnoreCaseOrCategoryNameAndBoardContentContainingIgnoreCase(
		String categoryName,
		String titleKeyword,
		String categoryName2,
		String contentKeyword,
		Pageable pageable
	);

	Page<BoardEntity> findAllByCategoryName(String categoryName, Pageable pageable);

	Optional<BoardEntity> findByBoardId(String boardId);

}
