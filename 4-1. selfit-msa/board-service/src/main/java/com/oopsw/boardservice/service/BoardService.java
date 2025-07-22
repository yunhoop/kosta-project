package com.oopsw.boardservice.service;

import java.util.List;

import com.oopsw.boardservice.dto.BoardDto;

public interface BoardService {

	BoardDto getBoard(BoardDto boardDto);

	List<BoardDto> getBoards(int page, String categoryName, String sortOrder, String keyword);

	void addBoard(BoardDto boardDto);

	void setBoard(BoardDto boardDto);

	void removeBoard(BoardDto boardDto);

	Boolean toggleBookmark(BoardDto boardDto);

	// BoardDto getBoardTotal();
}
