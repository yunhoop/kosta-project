package com.camp.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.camp.model.TagDAO;
import com.camp.model.TagVO;

public class GetCategoryTagAction implements Action{
	public String execute(HttpServletRequest request) throws ServletException, IOException {
		int categoryId = Integer.parseInt(request.getParameter("categoryId"));
		System.out.println(categoryId);
		
		TagDAO dao = new TagDAO();
		List<TagVO> tags = dao.getTagsByCategory(categoryId);
		
		request.setAttribute("tagList", tags);
		
		return "postTag.jsp";
	}
}
