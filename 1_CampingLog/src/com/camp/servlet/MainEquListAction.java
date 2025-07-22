package com.camp.servlet;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.camp.model.MainUIDAO;
import com.camp.model.PostVO;

public class MainEquListAction implements Action {

	@Override
	public String execute(HttpServletRequest request) throws ServletException, IOException {
		MainUIDAO dao = new MainUIDAO();
	    Collection<PostVO> list = dao.getPopularEquList(4);
        request.setAttribute("equList", list);
		return "mainEquList.jsp";
	}

}
