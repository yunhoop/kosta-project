package com.camp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;



public class WriteUIAction implements Action{

	@Override
	public String execute(HttpServletRequest request) throws ServletException, IOException {
		
		return "Write.jsp";
	}

}
