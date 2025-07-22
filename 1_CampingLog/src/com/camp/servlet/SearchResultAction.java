package com.camp.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.camp.model.PostDAO;
import com.camp.model.PostVO;

public class SearchResultAction implements Action {
	@Override
	public String execute(HttpServletRequest request) throws ServletException, IOException {
		String searchTerm=request.getParameter("searchTerm");
		System.out.println(searchTerm);
		// 파라미터 받기
		int page = Integer.parseInt(request.getParameter("page"));
		int pageSize = 4;


		int start = (page - 1) * pageSize + 1;
		int end = page * pageSize;

		System.out.println(start + " " + end);


		PostDAO dao = new PostDAO();
		List<PostVO> posts = dao.getPostPage(start, end, searchTerm);
		int totalPosts = dao.getTotalPostCount(searchTerm);

		System.out.println(posts);

		request.setAttribute("postList", posts);
		request.setAttribute("totalPostCount", totalPosts);
		request.setAttribute("currentPage", page);

		return "searchTermData.jsp"; // 
	}
}
