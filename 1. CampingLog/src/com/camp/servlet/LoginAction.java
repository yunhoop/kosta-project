package com.camp.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.camp.model.LoginDAO;
import com.camp.model.MemberVO;

public class LoginAction implements Action {
    @Override
    public String execute(HttpServletRequest request)
            throws ServletException, IOException {
        System.out.println("▶ LoginAction 진입");
        String id = request.getParameter("id");
        String pw = request.getParameter("pw");

        MemberVO vo = new MemberVO();
        vo.setMemberId(id);
        vo.setPw(pw);

        // 로그인 처리 후 MemberVO 객체 반환
        MemberVO member = new LoginDAO().login(vo);
        System.out.println("[LoginAction] 파라미터 id=" + id + ", pw=" + pw);
        System.out.println("[LoginAction] DAO 반환 member=" + member);

        // 로그인 성공 여부 체크: member 객체가 null이 아니고 nickname이 유효한지 확인
        boolean loginSuccess = (member != null && member.getNickName() != null
        		&& !member.getNickName().isEmpty());
        request.setAttribute("loginSuccess", loginSuccess);

        if (loginSuccess) {
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", member); // 로그인한 유저 정보를 세션에 저장
            System.out.println("[LoginAction] 세션에 사용자 정보 저장 완료");

            request.setAttribute("nickname", member.getNickName()); // nickname을 JSP로 전달
        }

        return "loginResult.jsp"; // 로그인 결과 페이지로 포워딩
    }
}
