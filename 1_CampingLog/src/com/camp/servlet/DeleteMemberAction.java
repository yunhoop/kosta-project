package com.camp.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.camp.model.MemberDAO;
import com.camp.model.MemberVO;

public class DeleteMemberAction implements Action {

    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        HttpSession session = request.getSession();

        MemberVO loginMember = (MemberVO) session.getAttribute("loginUser");
        if (loginMember == null) {
            request.setAttribute("result", "unauthorized");
            return "deleteMember.jsp";
        }

        String inputPw = request.getParameter("pw");
        String memberId = loginMember.getMemberId();

        MemberDAO dao = new MemberDAO();
        boolean deleted = dao.deleteMember(memberId, inputPw);

        if (deleted) {
            session.invalidate(); // 세션 제거
            request.setAttribute("result", "success");
        } else {
            request.setAttribute("result", "wrongPassword");
        }

        return "deleteMember.jsp";
    }
}
