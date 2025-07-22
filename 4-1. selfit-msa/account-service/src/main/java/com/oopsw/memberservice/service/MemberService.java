package com.oopsw.memberservice.service;

import java.util.List;

import com.oopsw.memberservice.dto.MemberDto;

public interface MemberService {
	void addMember(MemberDto memberDto);

	MemberDto getMember(String memberId);

	MemberDto getMemberByEmail(String email);

	void setMember(MemberDto memberDto);

	void removeMember(String memberId);

	Boolean checkEmail(MemberDto memberDto);

	Boolean checkNickname(MemberDto memberDto);

	Boolean checkPw(MemberDto memberDto);

	List<MemberDto> getMemberLike(MemberDto memberDto);
}
