package com.oopsw.memberservice.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.oopsw.memberservice.auth.user.CustomUserDetails;
import com.oopsw.memberservice.auth.user.User;
import com.oopsw.memberservice.jpa.MemberEntity;
import com.oopsw.memberservice.jpa.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		MemberEntity memberEntity = memberRepository.findByEmail(email);
		if (memberEntity == null) {
			throw new UsernameNotFoundException("아이디 및 비밀번호가 일치하지 않습니다.");
		}
		User user = User.builder()
			.memberId(memberEntity.getMemberId())
			.email(email)
			.pw(memberEntity.getPw())
			.nickname(memberEntity.getNickname())
			.build();

		return new CustomUserDetails(user);
	}
}
