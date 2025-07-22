package com.oopsw.boardservice.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {

	BookmarkEntity findByBoardIdAndMemberId(String boardId, String memberId);
}
