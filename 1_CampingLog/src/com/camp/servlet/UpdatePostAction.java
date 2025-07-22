package com.camp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.camp.model.PostVO;
import com.camp.service.PostService;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class UpdatePostAction implements Action {
    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {

        MultipartRequest multi = new MultipartRequest(
            request,
            request.getServletContext().getRealPath("/upload"),
            10 * 1024 * 1024,
            "UTF-8",
            new DefaultFileRenamePolicy()
        );

        int postId = Integer.parseInt(multi.getParameter("postId"));
        String title = multi.getParameter("postTitle");
        String contents = multi.getParameter("postContents");
        String image = multi.getFilesystemName("postImageFile");
        if (image == null || image.trim().isEmpty()) {
            image = multi.getParameter("originalImageName");
        }
        int categoryId = Integer.parseInt(multi.getParameter("categoryId"));
        String[] tagIds = multi.getParameterValues("tagId");

        PostVO vo = new PostVO(postId, title, contents, image, "user01", categoryId);

        PostService service = new PostService();
        boolean result = service.updatePostWithTags(vo, tagIds);
    
       
        request.setAttribute("result", result ? "success" : "fail");

      
        return "updatePost.jsp";
    }
}
