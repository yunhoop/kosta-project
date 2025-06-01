package com.camp.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.camp.model.TagDAO;
import com.camp.model.TagVO;

public class TagListAction implements Action {

    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));

        TagDAO dao = new TagDAO();
        List<TagVO> tags = dao.getTagsByCategory(categoryId);

        request.setAttribute("result", tags);  
        return "tagList.jsp";  
    }
}
