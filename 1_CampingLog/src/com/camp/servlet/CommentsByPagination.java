package com.camp.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSessionFactory;

import com.camp.model.CommentsVO;
import com.camp.model.DBCP;
import com.camp.service.DetailService;

public class CommentsByPagination implements Action {

	@Override
	public String execute(HttpServletRequest request) throws ServletException, IOException {
		SqlSessionFactory factory = DBCP.getSqlSessionFactory();
		int page = Integer.parseInt(request.getParameter("page"));
		int postId = Integer.parseInt(request.getParameter("postId"));
		int pageSize = 5;
		
		int start = (page - 1) * pageSize + 1;
		int end = page * pageSize;
		
		
		//ds factory Ãß°¡
		DetailService ds = new DetailService(factory);
		List<CommentsVO> comments = ds.getCommentsByPagination(postId, start, end);
		int commentCount = ds.getCommentCount(postId);
		
		
		request.setAttribute("commentList", comments);
		request.setAttribute("commentCount", commentCount);
		request.setAttribute("currentComment", page);
		
		return "comments.jsp";
	}

}
