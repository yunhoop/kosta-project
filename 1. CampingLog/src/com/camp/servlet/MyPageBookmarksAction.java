package com.camp.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.camp.model.MemberDAO;
import com.camp.model.MemberVO;

public class MyPageBookmarksAction implements Action {
    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            request.setAttribute("error", "unauthorized");
            return "myPageBookmarks.jsp";
        }
        MemberVO user = (MemberVO) session.getAttribute("loginUser");
        List<HashMap<String, Object>> bookmarks = new MemberDAO().getMyBookmarks(user.getMemberId());
        request.setAttribute("bookmarks", bookmarks);
        return "myPageBookmarks.jsp";
    }
}
