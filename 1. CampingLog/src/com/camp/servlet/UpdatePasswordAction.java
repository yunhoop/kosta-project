package com.camp.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.camp.model.MemberDAO;
import com.camp.model.MemberVO;

public class UpdatePasswordAction implements Action {
    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(false);
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        String newPw = request.getParameter("pw");
        boolean result = false;

        if (loginUser != null && newPw != null && !newPw.isEmpty()) {
            MemberDAO dao = new MemberDAO();
            result = dao.updatePassword(loginUser.getMemberId(), newPw);
        }

        request.setAttribute("result", result ? "success" : "fail");
        return "updatePasswordResult.jsp";
    }
}
