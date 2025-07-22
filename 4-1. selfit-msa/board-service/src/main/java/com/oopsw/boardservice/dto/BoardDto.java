package com.oopsw.boardservice.dto;

import java.util.Date;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {
	private Long id;
	private String boardTitle;
	private String boardContent;
	private String categoryName;
	private String boardImg;
	private Date createdDate;
	private String nickName;
	private int viewCount;
	private int commentCount;
	private int totalCount;
	private String boardId;
	private String memberId;
}
