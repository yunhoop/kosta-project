package com.camp.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSessionFactory;

import com.camp.model.CommentsVO;
import com.camp.model.DBCP;
import com.camp.model.PostVO;
import com.camp.service.DetailService;

public class DetailAction implements Action {

	@Override
	public String execute(HttpServletRequest request) throws ServletException, IOException {
		int postId=Integer.parseInt(request.getParameter("postId"));
		SqlSessionFactory factory = DBCP.getSqlSessionFactory();
		DetailService service = new DetailService(factory);
		
		PostVO post = service.getPostDetails(postId);
		List<String> tagList = service.getTagsForPost(postId);
		List<CommentsVO> commentList = service.getCommentsForPost(postId);
		
		request.setAttribute("post", post);
		request.setAttribute("tagList", tagList);
		
		return "detail.jsp";
	}

}
