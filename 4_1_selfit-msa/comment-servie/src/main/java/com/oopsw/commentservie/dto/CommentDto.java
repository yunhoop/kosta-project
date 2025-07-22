package com.oopsw.commentservie.dto;

import java.util.Date;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class CommentDto {
	private Long id;
	private String commentId;
	private String commentContent;
	private Date createdDate;
	private String memberId;
	private String boardId;

	private String nickname;
	private String profileImg;
}
