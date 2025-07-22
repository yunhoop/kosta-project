package com.camp.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LogoutAction implements Action {

    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // 이미 존재하는 세션만
        if (session != null) {
            session.invalidate(); // 세션 종료
        }
        return "mainUI.html"; 
    }
}
