package com.oopsw.boardservice.vo.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResGetBoards {
	private String boardId;
	private String boardTitle;
	private String categoryName;
	private Date createdDate;
	private int viewCount;
	private int totalCount;
	private String nickName;
}
