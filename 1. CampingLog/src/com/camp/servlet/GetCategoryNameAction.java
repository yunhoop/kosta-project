package com.camp.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.camp.model.PostDAO;
import com.camp.model.PostVO;

public class GetCategoryNameAction implements Action {

	@Override
	public String execute(HttpServletRequest request) throws ServletException, IOException {
		
		PostDAO dao = new PostDAO();
		List<PostVO> list = dao.getCategoryName();
		request.setAttribute("categoryList", list);
		
		return "header.jsp";
	}

}
