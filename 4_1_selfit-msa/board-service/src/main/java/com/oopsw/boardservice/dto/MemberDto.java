package com.oopsw.boardservice.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class MemberDto implements Serializable {
	private String memberId;
	private String nickname;
}
