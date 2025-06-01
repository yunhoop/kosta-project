package com.camp.servlet;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.camp.model.MemberVO;

import com.camp.model.MemberDAO;

public class PasswordCheckAction implements Action {
    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String pw = request.getParameter("pw");

        HttpSession session = request.getSession(false);
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        boolean verified = false;

        if (loginUser != null && pw != null) {
            MemberDAO dao = new MemberDAO();
            String dbPw = dao.getPasswordById(loginUser.getMemberId());

            if (dbPw != null && pw.equals(dbPw)) {
                verified = true;
            }
        }

        request.setAttribute("verified", verified);
        return "passwordCheckResult.jsp";
    }
}
