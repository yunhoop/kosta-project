package com.camp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.camp.model.MemberDAO;
import com.camp.model.MemberVO;

public class SignUpAction implements Action {
    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String pw = request.getParameter("pw");
        String email = request.getParameter("email");
        String nickName = request.getParameter("nickName");
        String name = request.getParameter("name");
        String phoneNumber = request.getParameter("phoneNumber");

        MemberDAO dao = new MemberDAO();

        //백엔드에서 중복 체크 추가
        if (dao.isDuplicateId(id) || dao.isDuplicateEmail(email) || dao.isDuplicateNickName(nickName)) {
            request.setAttribute("result", "duplicate");
            return "signUpResult.jsp";
        }

        MemberVO vo = new MemberVO();
        vo.setMemberId(id);
        vo.setPw(pw);
        vo.setEmail(email);
        vo.setNickName(nickName);
        vo.setName(name);
        vo.setPhoneNumber(phoneNumber);
        vo.setGradeId(1); // 기본 등급

        boolean isAdded = dao.addMember(vo);

        if (isAdded) {
            request.getSession().setAttribute("loginUser", vo);
        }

        request.setAttribute("result", isAdded ? "success" : "fail");
        request.setAttribute("nickname", vo.getNickName());

        return "signUpResult.jsp";
    }
}


