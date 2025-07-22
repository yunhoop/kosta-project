package com.camp.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.camp.service.PostService;

public class GetPostDetailAction implements Action {

	@Override
	public String execute(HttpServletRequest request) throws ServletException, IOException {
		
		System.out.println("GetPostDetailAction 진입 성공");

		if (request.getParameter("postId") == null) {
			System.out.println("postId 파라미터 없음");
			request.setAttribute("error", "Invalid request: postId is required.");
			return null; // 또는 에러 페이지 경로
		}

		int postId = Integer.parseInt(request.getParameter("postId"));
		System.out.println("postId 파라미터: " + postId);


		PostService service = new PostService();
		Map<String, Object> detail = service.getPostDetail(postId);
		System.out.println("service.getPostDetail() 결과: " + detail);

		

		request.setAttribute("result", detail);
		return "getPostDetail.jsp";

	}
}
