package com.oopsw.boardservice.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oopsw.boardservice.dto.BoardDto;
import com.oopsw.boardservice.service.BoardService;
import com.oopsw.boardservice.vo.request.ReqAddBoard;
import com.oopsw.boardservice.vo.request.ReqSetBoard;
import com.oopsw.boardservice.vo.response.ResBookmarks;
import com.oopsw.boardservice.vo.response.ResGetBoard;
import com.oopsw.boardservice.vo.response.ResGetBoards;
import com.oopsw.boardservice.vo.response.ResMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board-service")
public class BoardController {

	private final Environment environment;
	private final ModelMapper modelMapper;
	private final BoardService boardService;

	@GetMapping("/rest_check")
	public Map<String, String> restCheck() {
		return Map.of("message", environment.getProperty("shop.message") + "Board check");
	}

	@PostMapping("/member/{memberId}")
	public ResponseEntity<ResMessage> addBoard(@RequestBody ReqAddBoard reqAddBoard, @PathVariable String memberId){
		BoardDto boardDto = modelMapper.map(reqAddBoard, BoardDto.class);
		boardDto.setMemberId(memberId);
		boardService.addBoard(boardDto);
		return ResponseEntity.ok(new ResMessage("success"));
	}

	@GetMapping("/list")
	public ResponseEntity<List<ResGetBoards>> getBoards(@RequestParam int page,
														@RequestParam String categoryName,
														@RequestParam String sortOrder,
														@RequestParam String keyword){
		log.info("getBoards - page: {}, categoryName: {}, keyword: {}, sortOrder: {}", page, categoryName, keyword,
			sortOrder);

		List<BoardDto> boardDto = boardService.getBoards(page, categoryName, sortOrder, keyword);
		List<ResGetBoards> resGetBoard = boardDto.stream()
			.map(dto -> modelMapper.map(dto, ResGetBoards.class))
			.collect(Collectors.toList());
		return ResponseEntity.ok(resGetBoard);
	}

	@GetMapping("/bookmark/{page}/member/{memberId}")
	public ResponseEntity<List<ResBookmarks>> getBookmarks(@PathVariable int page, @PathVariable String memberId){

		log.info("getBookmarks - page: {}, memberId: {}", page, memberId);


		return null;
	}

	@GetMapping("/{boardId}/member/{memberId}")
	public ResponseEntity<ResGetBoard> getBoard(@PathVariable String boardId,
												@PathVariable String memberId){
		BoardDto boardDto = BoardDto.builder()
			.boardId(boardId)
			.memberId(memberId)
			.build();
		log.info("getBoard - memberId: {}, boardId: {}", memberId, boardId);

		ResGetBoard resGetBoard =modelMapper.map(boardService.getBoard(boardDto), ResGetBoard.class);
		log.info("resGetBoard - resGetBoard: {}", resGetBoard);
		return ResponseEntity.ok(resGetBoard);
	}

	@PutMapping("/update/member/{memberId}")
	public ResponseEntity<ResMessage> setBoard(@PathVariable String memberId, @RequestBody ReqSetBoard reqSetBoard){
		BoardDto boardDto = modelMapper.map(reqSetBoard, BoardDto.class);
		boardDto.setMemberId(memberId);
		boardService.setBoard(boardDto);
		return ResponseEntity.ok(new ResMessage("success"));
	}

	@DeleteMapping("/{boardId}/member/{memberId}")
	public ResponseEntity<ResMessage> removeBoard(@PathVariable String boardId, @PathVariable String memberId){
		BoardDto boardDto = BoardDto.builder()
			.boardId(boardId)
			.memberId(memberId)
			.build();

		System.out.println(boardDto);
		boardService.removeBoard(boardDto);

		return ResponseEntity.ok(new ResMessage("success"));
	}

	@PutMapping("/bookmark/{boardId}/member/{memberId}")
	public ResponseEntity<ResMessage> toggleBookmark(@PathVariable String boardId, @PathVariable String memberId){
		BoardDto boardDto = BoardDto.builder()
			.boardId(boardId)
			.memberId(memberId)
			.build();
		System.out.println(boardDto);
		Boolean isBookmark = boardService.toggleBookmark(boardDto);
		return ResponseEntity.ok(new ResMessage(isBookmark.toString()));
	}


	// @GetMapping("/total")
	// public ResponseEntity<ResGetBoardTotal> getBoardTotal(){
	//
	// 	ResGetBoardTotal resGetBoardTotal =boardService.getBoardTotal();
	// 	return ResponseEntity.ok(new ResGetBoardTotal());
	// }


}
