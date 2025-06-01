package com.camp.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.camp.model.MemberVO;

public class MyPageAction implements Action {
    @Override
    public String execute(HttpServletRequest req)
            throws ServletException {
        HttpSession session = req.getSession(false);
        MemberVO login = (session != null)
            ? (MemberVO) session.getAttribute("loginUser")   // ← 여기 키도 동일해야
            : null;
        System.out.println("[MyPageUIAction] loginUser=" + login);
        if (login == null) {
            // 로그인 안 됐으면 login.html 로 redirect
            return "login.html";
        }
        // 세션에 있는 사용자 객체를 JSP에서 쓰려면 request에도 담아두기
        req.setAttribute("memberInfo", login);
        return "myPage.html";
    }
}



