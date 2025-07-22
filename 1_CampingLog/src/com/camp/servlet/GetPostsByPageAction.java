package com.camp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import com.camp.model.PostDAO;
import com.camp.model.PostVO;
import com.google.gson.Gson;

public class GetPostsByPageAction implements Action {

	@Override
	public String execute(HttpServletRequest request) throws ServletException, IOException {
		// 파라미터 받기
		int page = Integer.parseInt(request.getParameter("page"));
		int categoryId = Integer.parseInt(request.getParameter("categoryId"));
		String tagList = request.getParameter("tagList");      // ex) "무료,반려동물"
		if (tagList == null) tagList = ""; 
		int pageSize = 4;

		int start = (page - 1) * pageSize + 1;
		int end = page * pageSize;

		System.out.println(categoryId);
		System.out.println(tagList);
		System.out.println(start + " " + end);

		PostDAO dao = new PostDAO();
		List<PostVO> posts = dao.getPostPageByCategory(categoryId, tagList , start, end);
		int totalPosts = dao.getTotalPostCountByCategory(categoryId, tagList);

		System.out.println(posts);

		request.setAttribute("postList", posts);
		request.setAttribute("totalPostCount", totalPosts);
		request.setAttribute("currentPage", page);

		return "postData.jsp"; // 
	}
}