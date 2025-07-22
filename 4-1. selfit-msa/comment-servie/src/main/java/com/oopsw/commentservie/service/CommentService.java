package com.oopsw.commentservie.service;

import java.util.List;

import com.oopsw.commentservie.dto.CommentDto;

public interface CommentService {

	List<CommentDto> getComments(String boardId, int page);

	void addComment(CommentDto commentDto);
}
