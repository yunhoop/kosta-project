package com.camp.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.camp.model.MemberVO;
import com.camp.model.PostVO;
import com.camp.service.PostService;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class WriteAction implements Action {

    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {

        // 세션에서 로그인 사용자 꺼내기
        HttpSession session = request.getSession();
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        if (loginUser == null) {
            request.setAttribute("result", "unauthorized");
            return "write.jsp"; // 로그인 안 된 경우 처리
        }

        String uploadPath = request.getServletContext().getRealPath("/upload");
        int maxSize = 10 * 1024 * 1024;

        MultipartRequest multi = new MultipartRequest(
            request,
            uploadPath,
            maxSize,
            "UTF-8",
            new DefaultFileRenamePolicy()
        );

        String image = multi.getFilesystemName("postImageFile");
        String title = multi.getParameter("postTitle");
        String contents = multi.getParameter("postContents");
        String memberId = loginUser.getMemberId(); // 세션에서 꺼낸 ID 사용
        int categoryId = Integer.parseInt(multi.getParameter("categoryId"));
        String[] tagIds = multi.getParameterValues("tagId");

        PostVO vo = new PostVO(title, contents, image, memberId, categoryId);

        if (image != null) {
            File src = new File(uploadPath, image);
            File dest = new File("C:/OOPSW/workspace/CampingLog_test/WebContent/upload", image); 
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        PostService service = new PostService();
        boolean result = service.registerPostWithTags(vo, tagIds);

        request.setAttribute("result", result ? "success" : "fail");

        return "write.jsp";
    }
}
