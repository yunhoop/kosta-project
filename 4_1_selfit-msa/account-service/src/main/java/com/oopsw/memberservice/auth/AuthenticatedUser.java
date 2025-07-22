package com.oopsw.memberservice.auth;

public interface AuthenticatedUser {
	String getMemberId();

	String getEmail();

	String getNickname();
}
