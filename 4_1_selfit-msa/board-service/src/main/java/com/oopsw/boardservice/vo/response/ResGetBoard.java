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
public class ResGetBoard {
	private String memberId;
	private String boardTitle;
	private String boardContent;
	private String categoryName;
	private String nickName;
	private String boardImg;
	private Date createdDate;
	private int commentCount;
	private int viewCount;

}
