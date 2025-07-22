package com.oopsw.memberservice.auth.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
	private final String role = "ROLE_USER";
	private String memberId;
	private String nickname;
	private String email;
	private String pw;

}
