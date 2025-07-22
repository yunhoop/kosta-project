package com.oopsw.memberservice.auth.jwt;

import com.oopsw.memberservice.dto.MemberDto;

import lombok.Data;

@Data
public class AuthResult {
	private boolean success;
	private String message;
	private MemberDto member;
	private String accessToken;
	private String refreshToken;
}
