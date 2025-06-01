package com.camp.servlet;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.camp.model.MainUIDAO;
import com.camp.model.MemberVO;

public class MemberRankListAction implements Action {

	@Override
	public String execute(HttpServletRequest request) throws ServletException, IOException {
        MainUIDAO dao = new MainUIDAO();
        Collection<MemberVO> rankList = dao.getTopMembersByLikes(5);
        request.setAttribute("rankList", rankList);
        return "mainRankList.jsp";
	}

}
