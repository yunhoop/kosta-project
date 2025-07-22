package com.oopsw.commentservie.vo.response;

import java.util.Date;

import lombok.Data;

@Data
public class ResGetComments {
	private String commentContent;
	private Date createdDate;
	private String memberId;

	private String nickname;
	private String profileImg;
}
