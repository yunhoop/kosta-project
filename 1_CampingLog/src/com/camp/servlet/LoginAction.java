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
        System.out.println("�� LoginAction ����");
        String id = request.getParameter("id");
        String pw = request.getParameter("pw");

        MemberVO vo = new MemberVO();
        vo.setMemberId(id);
        vo.setPw(pw);

        // �α��� ó�� �� MemberVO ��ü ��ȯ
        MemberVO member = new LoginDAO().login(vo);
        System.out.println("[LoginAction] �Ķ���� id=" + id + ", pw=" + pw);
        System.out.println("[LoginAction] DAO ��ȯ member=" + member);

        // �α��� ���� ���� üũ: member ��ü�� null�� �ƴϰ� nickname�� ��ȿ���� Ȯ��
        boolean loginSuccess = (member != null && member.getNickName() != null
        		&& !member.getNickName().isEmpty());
        request.setAttribute("loginSuccess", loginSuccess);

        if (loginSuccess) {
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", member); // �α����� ���� ������ ���ǿ� ����
            System.out.println("[LoginAction] ���ǿ� ����� ���� ���� �Ϸ�");

            request.setAttribute("nickname", member.getNickName()); // nickname�� JSP�� ����
        }

        return "loginResult.jsp"; // �α��� ��� �������� ������
    }
}
