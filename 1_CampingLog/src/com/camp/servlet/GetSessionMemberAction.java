package com.camp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.camp.model.MemberVO;

public class GetSessionMemberAction implements Action {

	@Override
	public String execute(HttpServletRequest request) throws ServletException, IOException {
		 HttpSession session = request.getSession();
	        MemberVO user = (MemberVO) session.getAttribute("loginUser");

	        if (user == null) {
	            request.setAttribute("memberId", ""); // 로그인 안 된 상태
	            request.setAttribute("memberImage", "");
	        } else {
	            request.setAttribute("memberId", user.getMemberId());
	            request.setAttribute("memberImage", user.getMemberImage());
	        }

	        return "sessionMember.jsp"; // → JSON 응답용 JSP
	}

}
