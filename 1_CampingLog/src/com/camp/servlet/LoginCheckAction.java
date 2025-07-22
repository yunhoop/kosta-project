package com.camp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.camp.model.MemberVO;

public class LoginCheckAction implements Action {

	@Override
	public String execute(HttpServletRequest request) throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		boolean loggedIn = false;
		String profileImage = "";

		MemberVO user = (MemberVO) session.getAttribute("loginUser");
		if (user != null) {
			loggedIn = true;
			System.out.println("loggedIn : " + loggedIn);
			profileImage = user.getMemberImage();
			System.out.println("profileImage : " + profileImage);
			System.out.println("getNickName : " + user.getNickName());
			System.out.println("profileImage : " + profileImage);
		}

		request.setAttribute("loggedIn", loggedIn);
		request.setAttribute("profileImage", profileImage);
		return "loginCheckResult.jsp";
	}

}
