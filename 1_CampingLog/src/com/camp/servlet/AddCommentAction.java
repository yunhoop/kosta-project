package com.camp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSessionFactory;

import com.camp.model.CommentsVO;
import com.camp.model.DBCP;
import com.camp.service.DetailService;

public class AddCommentAction implements Action {

	@Override
	public String execute(HttpServletRequest request) throws ServletException, IOException {
		SqlSessionFactory factory = DBCP.getSqlSessionFactory();
		DetailService service = new DetailService(factory);
		int postId = Integer.parseInt(request.getParameter("postId"));
		String commentContents = request.getParameter("commentContents");
		String memberId = request.getParameter("memberId");
		boolean addCheck = service.addComment(new CommentsVO(commentContents, postId, memberId));
		request.setAttribute("addCheck", addCheck);
		return "addComment.jsp";
	}

}
