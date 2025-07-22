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
            ? (MemberVO) session.getAttribute("loginUser")   // �� ���� Ű�� �����ؾ�
            : null;
        System.out.println("[MyPageUIAction] loginUser=" + login);
        if (login == null) {
            // �α��� �� ������ login.html �� redirect
            return "login.html";
        }
        // ���ǿ� �ִ� ����� ��ü�� JSP���� ������ request���� ��Ƶα�
        req.setAttribute("memberInfo", login);
        return "myPage.html";
    }
}



