package com.oopsw.commentservie.vo.request;

import lombok.Data;

@Data
public class ReqAddComment {
	private String commentContent;
	private String memberId;
}
