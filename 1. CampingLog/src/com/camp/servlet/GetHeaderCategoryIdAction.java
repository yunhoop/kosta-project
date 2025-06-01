package com.camp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.camp.model.PostDAO;

public class GetHeaderCategoryIdAction implements Action {

	@Override
	public String execute(HttpServletRequest request) throws ServletException, IOException {
		
		int categoryId = Integer.parseInt(request.getParameter("categoryId"));
		
		System.out.println(categoryId);
		
		return "header.jsp";
	}
}
