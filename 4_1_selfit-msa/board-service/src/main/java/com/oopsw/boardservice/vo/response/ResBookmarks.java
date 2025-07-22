package com.oopsw.boardservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResBookmarks {
	String boardId;
	String memberId;
	String boardTitle;
}
