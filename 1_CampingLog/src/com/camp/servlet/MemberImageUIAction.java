package com.camp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.camp.model.MemberDAO;

public class MemberImageUIAction implements Action {

	@Override
	public String execute(HttpServletRequest request) throws ServletException, IOException {
		MemberDAO member = new MemberDAO();
		String memberId = request.getParameter("memberId");
		
		String userImage = member.getMemberImage(memberId);
		
		request.setAttribute("userImage", userImage);
		
		System.out.println("memberImageUIAction : "+userImage);
		return "userImage.jsp";
	}

}
