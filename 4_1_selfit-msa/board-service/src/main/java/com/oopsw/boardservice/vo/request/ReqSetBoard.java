package com.oopsw.boardservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqSetBoard {
	private String boardId;
	private String boardTitle;
	private String boardContent;
	private String categoryName;
	private String boardImg;
}
