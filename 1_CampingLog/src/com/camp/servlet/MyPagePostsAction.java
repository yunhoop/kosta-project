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
        // 1) ������ �ٽ� ������ ���� false��, �α��� ������ null ����
        HttpSession session = request.getSession(false);
        MemberVO loginMember = (session != null)
            ? (MemberVO) session.getAttribute("loginUser")
            : null;

        // 2) �α��� �� �� ���¶�� JSON error ����
        if (loginMember == null) {
            request.setAttribute("error", "unauthorized");
            return "myPagePosts.jsp";  
            // -> �� JSP�� {"error":"unauthorized"} �� ����ϵ��� ����� �Ӵϴ�.
        }

        // 3) �α��� �� ���¿����� DAO ȣ��
        MemberDAO dao = new MemberDAO();
        List<HashMap<String, Object>> posts = dao.getMyPosts(loginMember.getMemberId());

        // 4) ����� JSP ��� ����
        request.setAttribute("posts", posts);
        return "myPagePosts.jsp";
    }
}

