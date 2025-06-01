package com.camp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.camp.model.MainUIDAO;

public class MainRankUpAction implements Action {

	@Override
	public String execute(HttpServletRequest request) throws ServletException, IOException {
        MainUIDAO dao = new MainUIDAO();
        dao.updateAllMemberGrade();
        request.setAttribute("result", "success");
        return "mainRankUp.jsp"; 
	}

}
