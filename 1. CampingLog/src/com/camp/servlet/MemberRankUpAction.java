package com.camp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.camp.model.MainUIDAO;

public class MemberRankUpAction implements Action {

	@Override
	public String execute(HttpServletRequest request) throws ServletException, IOException {
        String memberId = request.getParameter("memberId");

        if (memberId != null && !memberId.trim().isEmpty()) {
            MainUIDAO dao = new MainUIDAO();
            dao.updateMemberGradeById(memberId);
            request.setAttribute("result", "success");
        } else {
            request.setAttribute("result", "fail");
        }

        return "memberRankUp.jsp";
	}

}
