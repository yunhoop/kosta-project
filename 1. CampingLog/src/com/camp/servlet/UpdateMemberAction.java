package com.camp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.camp.model.MemberDAO;
import com.camp.model.MemberVO;

public class UpdateMemberAction implements Action {

    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        MemberVO loginMember = (MemberVO) session.getAttribute("loginUser");

        if (loginMember == null) {
            request.setAttribute("result", "unauthorized");
            return "updateMember.jsp";
        }

        String email = request.getParameter("email");
        String nickname = request.getParameter("nickname");
        String name = request.getParameter("name");
        String phoneNumber = request.getParameter("phoneNumber");

        MemberDAO dao = new MemberDAO();

        //  중복 이메일 검사 (현재 이메일과 다를 경우만)
        if (!email.equals(loginMember.getEmail()) && dao.isDuplicateEmail(email)) {
            request.setAttribute("result", "duplicate_email");
            return "updateMember.jsp";
        }

        // 중복 닉네임 검사 (현재 닉네임과 다를 경우만)
        if (!nickname.equals(loginMember.getNickName()) && dao.isDuplicateNickName(nickname)) {
            request.setAttribute("result", "duplicate_nickname");
            return "updateMember.jsp";
        }

        // VO 구성
        MemberVO updatedMember = new MemberVO();
        updatedMember.setMemberId(loginMember.getMemberId());
        updatedMember.setEmail(email);
        updatedMember.setNickName(nickname);
        updatedMember.setName(name);
        updatedMember.setPhoneNumber(phoneNumber);

        boolean result = dao.updateMemberInfo(updatedMember);

        if (result) {
            MemberVO refreshed = dao.getMyInfo(updatedMember.getMemberId());
            session.setAttribute("loginUser", refreshed);
        }

        request.setAttribute("result", result ? "success" : "fail");
        return "updateMember.jsp";
    }
}
