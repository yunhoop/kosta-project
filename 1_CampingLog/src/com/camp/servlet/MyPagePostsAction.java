package com.camp.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.camp.model.MemberDAO;
import com.camp.model.MemberVO;
import com.camp.model.PostVO;

public class MyPagePostsAction implements Action {
    @Override
    public String execute(HttpServletRequest request)
            throws ServletException, IOException {
        // 1) 세션을 다시 가져올 때는 false로, 로그인 전에는 null 리턴
        HttpSession session = request.getSession(false);
        MemberVO loginMember = (session != null)
            ? (MemberVO) session.getAttribute("loginUser")
            : null;

        // 2) 로그인 안 된 상태라면 JSON error 응답
        if (loginMember == null) {
            request.setAttribute("error", "unauthorized");
            return "myPagePosts.jsp";  
            // -> 이 JSP는 {"error":"unauthorized"} 만 출력하도록 만들어 둡니다.
        }

        // 3) 로그인 한 상태에서만 DAO 호출
        MemberDAO dao = new MemberDAO();
        List<HashMap<String, Object>> posts = dao.getMyPosts(loginMember.getMemberId());

        // 4) 결과를 JSP 뷰로 전달
        request.setAttribute("posts", posts);
        return "myPagePosts.jsp";
    }
}

