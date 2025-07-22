package com.camp.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.camp.model.MainUIDAO;
import com.camp.model.MemberVO;

public class YesterdayRankListAction implements Action {

	@Override
	public String execute(HttpServletRequest request) throws ServletException, IOException {
        MainUIDAO dao = new MainUIDAO();

        // ���� ��¥�� ���ؼ� (yyyy-mm-dd ����)
        //String date = "2025-04-21"; 
        String date = request.getParameter("date");

        if (date == null || date.isEmpty()) {
            LocalDate yesterday = LocalDate.now().minusDays(1);
            date = yesterday.format(DateTimeFormatter.ISO_DATE);
        }
        System.out.println("���� ��¥: " + date);
        // ���� ��¥ �������� ��ŷ ��ȸ
        Collection<MemberVO> rankList = dao.getYesterdayTopMembersByLikes(date);
        
        request.setAttribute("rankList", rankList);
        request.setAttribute("rankDate", date); // �����ϵ� ���� �ѱ��
        return "mainRankList.jsp";
	}

}
