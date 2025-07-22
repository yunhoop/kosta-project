package com.camp.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.camp.model.MemberDAO;

public class CheckDuplicateAction implements Action {
    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        String field = request.getParameter("field"); // id, email, nickname
        String value = request.getParameter("value");

        if (field == null || value == null || value.trim().isEmpty()) {
            request.setAttribute("error", "Invalid parameters");
            return "checkDuplicateResult.jsp"; // 오류 시에도 JSON 응답 유지
        }

        boolean isDuplicate = false;
        MemberDAO dao = new MemberDAO();

        try {
            switch (field) {
                case "id":
                    isDuplicate = dao.isDuplicateId(value);
                    break;
                case "email":
                    isDuplicate = dao.isDuplicateEmail(value);
                    break;
                case "nickname":
                    isDuplicate = dao.isDuplicateNickName(value);
                    break;
                default:
                    request.setAttribute("error", "Invalid field type");
                    return "checkDuplicateResult.jsp";
            }

            request.setAttribute("isDuplicate", isDuplicate);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error occurred");
        }

        return "checkDuplicateResult.jsp";
    }
}
