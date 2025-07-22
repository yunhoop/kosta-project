package com.oopsw.boardservice.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.oopsw.boardservice.dto.BoardDto;
import com.oopsw.boardservice.dto.MemberDto;
import com.oopsw.boardservice.jpa.BoardEntity;
import com.oopsw.boardservice.jpa.BoardRepository;
import com.oopsw.boardservice.jpa.BookmarkEntity;
import com.oopsw.boardservice.jpa.BookmarkRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

	private final BoardRepository boardRepository;
	private final BookmarkRepository bookmarkRepository;
	private final ModelMapper modelMapper;
	private final Environment environment;
	private final RestTemplate restTemplate;

	private void validateBoardRequiredFields(BoardDto boardDto) {
		if (boardDto.getBoardTitle() == null || boardDto.getBoardTitle().isBlank()) {
			throw new IllegalArgumentException("제목은 필수입니다.");
		}
		if (boardDto.getBoardContent() == null || boardDto.getBoardContent().isBlank()) {
			throw new IllegalArgumentException("내용은 필수입니다.");
		}
		if (boardDto.getCategoryName() == null) {
			throw new IllegalArgumentException("카테고리를 선택해주세요.");
		}
	}

	@Override
	public BoardDto getBoard(BoardDto boardDto) {
		// 게시글 조회 및 조회수 증가
		BoardEntity boardEntity = boardRepository.findByBoardId(boardDto.getBoardId())
			.orElseThrow(() ->
				new IllegalArgumentException("게시글이 존재하지 않습니다.")
			);
		boardEntity.setViewCount(boardEntity.getViewCount() + 1);
		boardRepository.save(boardEntity);

		BoardDto resultBoard = modelMapper.map(boardEntity, BoardDto.class);

		// member-service 통신
		String url = String.format(environment.getProperty("member_service.url"), boardDto.getMemberId());
		resultBoard.setNickName(
			restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<MemberDto>() {})
				.getBody()
				.getNickname()
		);

		return resultBoard;
	}

	@Override
	public List<BoardDto> getBoards(int page, String categoryName, String sortOrder, String keyword) {
		int pageSize = 10;

		Sort.Direction dir = "asc".equalsIgnoreCase(sortOrder)
			? Sort.Direction.ASC
			: Sort.Direction.DESC;

		Pageable pageable = PageRequest.of(page -1, pageSize, Sort.by(dir, "createdDate"));

		Page<BoardEntity> pageResult;

		if(keyword != null && !keyword.isEmpty()) {
			pageResult = boardRepository.findByCategoryNameAndBoardTitleContainingIgnoreCaseOrCategoryNameAndBoardContentContainingIgnoreCase(
				categoryName, keyword,
				categoryName, keyword,
				pageable
			);
		} else {
			pageResult = boardRepository.findAllByCategoryName(categoryName, pageable);
		}

		int totalBoards = (int)pageResult.getTotalElements();

		if(pageResult.isEmpty()){
			return Collections.emptyList();
		}
		List<BoardDto> boardDtoList = pageResult.stream().map(
			entity -> {
				BoardDto boardDto = modelMapper
					.map(entity, BoardDto.class);
				boardDto.setTotalCount(totalBoards);

				// member-service 통신
				String url = String.format(environment.getProperty("member_service.url"), boardDto.getMemberId());
				boardDto.setNickName(
					restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<MemberDto>() {})
						.getBody()
						.getNickname()
				);

				return boardDto;
			})
			.collect(Collectors.toList());
		System.out.println("id확인 : "+boardDtoList.get(0));
		return boardDtoList;
	}

	// @Override
	// public BoardDto getBoardTotal() {
	//
	// 	BoardDto boardDto = boardRepository.find
	//
	// 	return null;
	// }

	@Override
	public void addBoard(BoardDto boardDto) {
		validateBoardRequiredFields(boardDto);

		// String lastId = boardRepository.findTopByOrderByBoardIdDesc()
		// 	.map(BoardEntity::getBoardId).orElse("b0000");
		//
		// int seq = Integer.parseInt(lastId.substring(1)) + 1;
		//
		// String newBoardId = String.format("b%04d", seq);

		boardDto.setBoardId(UUID.randomUUID().toString());
		BoardEntity entity = modelMapper.map(boardDto, BoardEntity.class);
		boardRepository.save(entity);
	}

	@Override
	public void setBoard(BoardDto boardDto) {
		validateBoardRequiredFields(boardDto);

		BoardEntity boardEntity = boardRepository.findByBoardId(boardDto.getBoardId())
				.orElseThrow(() ->
						new IllegalArgumentException("수정하려는 게시글이 존재하지 않습니다."));
		boardEntity.setCategoryName(boardDto.getCategoryName());
		boardEntity.setBoardTitle(boardDto.getBoardTitle());
		boardEntity.setBoardContent(boardDto.getBoardContent());
		System.out.println(boardEntity);
		boardRepository.save(boardEntity);
	}

	@Override
	public void removeBoard(BoardDto boardDto) {
		System.out.println(boardDto);
		BoardEntity boardEntity = boardRepository.findByBoardId(boardDto.getBoardId())
			.orElseThrow(()->
				new IllegalArgumentException("삭제하려는 게시글이 존재하지 않습니다."));

		if (boardEntity.getBoardId().equals(boardDto.getBoardId())) {
			boardRepository.delete(boardEntity);
		}
	}

	@Override
	public Boolean toggleBookmark(BoardDto boardDto) {
		if (boardDto.getMemberId() == null) {
			throw new IllegalArgumentException("로그인을 해주세요.");
		}

		BookmarkEntity bookmarkEntity = bookmarkRepository
			.findByBoardIdAndMemberId(boardDto.getBoardId(), boardDto.getMemberId());

		if (bookmarkEntity != null) {
			bookmarkRepository.delete(bookmarkEntity);
			return true;
		} else {
			bookmarkRepository.save(modelMapper.map(boardDto, BookmarkEntity.class));
			return false;
		}
	}
}
