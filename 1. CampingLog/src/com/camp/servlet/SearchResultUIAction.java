package com.camp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class SearchResultUIAction implements Action {

	@Override
	public String execute(HttpServletRequest request) throws ServletException, IOException {
		return "searchResult.html";
	}


}
