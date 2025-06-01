package com.camp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSessionFactory;

import com.camp.model.DBCP;
import com.camp.service.DetailService;

public class AddBookmarkAction implements Action {

	@Override
	public String execute(HttpServletRequest request) throws ServletException, IOException {
		SqlSessionFactory factory = DBCP.getSqlSessionFactory();
		DetailService service = new DetailService(factory);
		
		String postIdStr = request.getParameter("postId");
		if (postIdStr == null) {
		    return "error.jsp"; // 예외 처리하거나, 일단 리턴
		}
		int postId = Integer.parseInt(request.getParameter("postId"));
		String memberId = request.getParameter("memberId");
		boolean addCheck = service.InsertBookmark(postId, memberId);
		
		request.setAttribute("addCheck", addCheck);
		return "addBookmark.jsp";
	}

}
