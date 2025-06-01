package com.camp.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.camp.service.PostService;

public class DeletePostAction implements Action {

    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        int postId = Integer.parseInt(request.getParameter("postId"));
        PostService service = new PostService();

        boolean result = service.deletePostWithTags(postId);

        request.setAttribute("result", result ? "success" : "fail");
        return "deletePost.jsp"; 
    }
}
