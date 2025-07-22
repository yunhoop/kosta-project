package com.oopsw.commentservie.service;

import java.lang.reflect.Member;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.oopsw.commentservie.dto.CommentDto;
import com.oopsw.commentservie.dto.MemberDto;
import com.oopsw.commentservie.jpa.CommentEntity;
import com.oopsw.commentservie.jpa.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;
	private final ModelMapper modelMapper;
	private final Environment environment;
	private final RestTemplate restTemplate;

	@Override
	public List<CommentDto> getComments(String boardId, int page) {
		Pageable pageable = PageRequest.of(page-1, 5, Sort.by(Sort.Direction.DESC, "createdDate"));
		return commentRepository.findByBoardId(boardId, pageable)
			.stream()
			.map(commentEntity -> {
				// 1. Entity → DTO 변환
				CommentDto commentDto = modelMapper.map(commentEntity, CommentDto.class);

				// 2. member-service 호출
				String url = String.format(environment.getProperty("member_service.url"), commentEntity.getMemberId());
				ResponseEntity<MemberDto> member = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<MemberDto>() { });
				MemberDto memberDto = member.getBody();
				commentDto.setNickname(memberDto.getNickname());
				commentDto.setProfileImg(memberDto.getProfileImg());

				// String nickname = restTemplate.getForEntity(url, MemberDto.class).getBody().getNickname();
				// String profileImg = restTemplate.getForEntity(url, MemberDto.class).getBody().getProfileImg();

				// 3. DTO 세팅
				// commentDto.setNickname(nickname);
				// commentDto.setProfileImg(profileImg);

				return commentDto;
			})
			.toList();
	}


	@Override
	public void addComment(CommentDto commentDto) {
		CommentEntity commentEntity = modelMapper.map(commentDto, CommentEntity.class);
		commentEntity.setCommentId(UUID.randomUUID().toString());
		if(commentDto.getMemberId() != null) {
			commentRepository.save(commentEntity);
		}
	}
}
