package com.camp.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.camp.model.NoticeDAO;
import com.camp.model.NoticeVO;

public class NoticeAction implements Action {

    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        NoticeDAO dao = new NoticeDAO(); // ������ DAO ���ο��� ó��
        List<NoticeVO> noticeList = dao.getNoticeList(); 
        request.setAttribute("noticeList", noticeList);
        return "notice.jsp";
    }
}
