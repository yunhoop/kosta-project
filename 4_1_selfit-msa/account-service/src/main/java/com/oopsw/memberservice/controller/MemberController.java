package com.oopsw.memberservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oopsw.memberservice.dto.MemberDto;
import com.oopsw.memberservice.service.MemberService;
import com.oopsw.memberservice.vo.request.ReqAddMember;
import com.oopsw.memberservice.vo.request.ReqCheckEmail;
import com.oopsw.memberservice.vo.request.ReqCheckNickname;
import com.oopsw.memberservice.vo.request.ReqCheckPw;
import com.oopsw.memberservice.vo.request.ReqSetMember;
import com.oopsw.memberservice.vo.response.ResGetMember;
import com.oopsw.memberservice.vo.response.ResGetMemberLike;
import com.oopsw.memberservice.vo.response.ResMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member-service")
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/api-test")
	public String test() {
		return "test";
	}

	@GetMapping("/member/{memberId}")
	public ResponseEntity<ResGetMember> getMember(@PathVariable String memberId) {
		return ResponseEntity.ok(new ModelMapper().map(memberService.getMember(memberId), ResGetMember.class));
	}

	@PostMapping("/member")
	public ResponseEntity<ResMessage> addMember(@RequestBody ReqAddMember reqAddMember) {
		memberService.addMember(new ModelMapper().map(reqAddMember, MemberDto.class));
		return ResponseEntity.ok(new ResMessage("success"));
	}

	@PutMapping("/member/{memberId}")
	public ResponseEntity<ResMessage> setMember(@PathVariable String memberId, @RequestBody ReqSetMember reqSetMember) {
		MemberDto memberDto = new ModelMapper().map(reqSetMember, MemberDto.class);
		memberDto.setMemberId(memberId);
		memberService.setMember(memberDto);
		return ResponseEntity.ok(new ResMessage("success"));
	}

	@DeleteMapping("/member/{memberId}")
	public ResponseEntity<ResMessage> removeMember(@PathVariable String memberId) {
		memberService.removeMember(memberId);
		return ResponseEntity.ok(new ResMessage("success"));
	}

	@PostMapping("/check-email")
	public ResponseEntity<ResMessage> checkEmail(@RequestBody ReqCheckEmail reqCheckEmail) {
		return ResponseEntity.ok(new ResMessage(memberService.checkEmail(new ModelMapper().map(reqCheckEmail, MemberDto.class)).toString()));
	}

	@PostMapping("/check-nickname")
	public ResponseEntity<ResMessage> checkNickname(@RequestBody ReqCheckNickname reqCheckNickname) {
		return ResponseEntity.ok(new ResMessage(memberService.checkNickname(new ModelMapper().map(reqCheckNickname, MemberDto.class)).toString()));
	}

	@PostMapping("/check-pw/member/{memberId}")
	public ResponseEntity<ResMessage> checkPw(@PathVariable String memberId, @RequestBody ReqCheckPw reqCheckPw) {
		MemberDto memberDto = new ModelMapper().map(reqCheckPw, MemberDto.class);
		memberDto.setMemberId(memberId);
		return ResponseEntity.ok(new ResMessage(memberService.checkPw(memberDto).toString()));
	}

	@GetMapping("/like/member/{memberId}")
	public ResponseEntity<List<ResGetMemberLike>> getMemberLike(@PathVariable String memberId) {
		MemberDto memberDto = MemberDto.builder().memberId(memberId).build();
		List<MemberDto> resultDto = memberService.getMemberLike(memberDto);
		List<ResGetMemberLike> response = new ArrayList<>();
		for(MemberDto memberDto1 : resultDto) {
			ResGetMemberLike resGetMemberLike = new ResGetMemberLike();
			resGetMemberLike.setMemberId(memberDto1.getMemberId());
			response.add(resGetMemberLike);
		}
		return ResponseEntity.ok(response);
	}

}
