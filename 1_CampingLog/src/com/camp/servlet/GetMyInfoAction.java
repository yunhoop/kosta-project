package com.camp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.camp.model.MemberDAO;
import com.camp.model.MemberVO;

public class GetMyInfoAction implements Action {
    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
    	System.out.println(" GetMyInfoAction 진입");

        HttpSession session = request.getSession();
        MemberVO loginMember = (MemberVO) session.getAttribute("loginUser"); 

        if (loginMember == null) {
            System.out.println("loginUser");
            request.setAttribute("error", "로그인이 필요합니다");
            return "login.html"; 
        }

        System.out.println(" 세션 ID: " + loginMember.getMemberId());


        MemberDAO dao = new MemberDAO();
        MemberVO updatedInfo = dao.getMyInfo(loginMember.getMemberId());

        if (updatedInfo == null) {
            System.out.println("DAO에서 정보가져오기 실패");
        } else {
            System.out.println("DAO에서 가져온 정보: " + updatedInfo);
        }

        // 諭껋� �씠誘몄� 怨꾩궛
        int likeCount = updatedInfo.getLikeCount();
        System.out.println("좋아요 : " + likeCount);

        String badge;
        if (likeCount >= 100) badge = "grade4.png";
        else if (likeCount >= 50) badge = "grade3.png";
        else if (likeCount >= 10) badge = "grade2.png";
        else badge = "grade1.png";

        updatedInfo.setBadgeImage(badge);
        System.out.println("뱃지: " + badge);

        session.setAttribute("loginUser", updatedInfo);
        request.setAttribute("memberInfo", updatedInfo);

        System.out.println(" request session");
        return "myPageInfo.jsp"; 
    }
}
