package com.camp.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.camp.model.MemberDAO;
import com.camp.model.MemberVO;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class AddProfilePhotoAction implements Action {

    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        String savePath = request.getServletContext().getRealPath("/upload/profile");
        int maxSize = 5 * 1024 * 1024; // 5MB
        String encoding = "UTF-8";

        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = null;
        String memberId = null;

        try {
            MultipartRequest multi = new MultipartRequest(
                request,
                savePath,
                maxSize,
                encoding,
                new DefaultFileRenamePolicy()
            );

            fileName = multi.getFilesystemName("photo");

            //  로그인된 사용자 정보에서 memberId 꺼내기
            HttpSession session = request.getSession();
            MemberVO loginMember = (MemberVO) session.getAttribute("loginUser");

            if (loginMember == null) {
                System.out.println("로그인 세션 없음");
                request.setAttribute("result", "unauthorized");
                request.setAttribute("fileName", null);
                return "addProfileImg";
            }

            memberId = loginMember.getMemberId();


            MemberDAO dao = new MemberDAO();
            boolean result = dao.updateProfileImage(memberId, fileName);

            request.setAttribute("result", result ? "success" : "fail");
            request.setAttribute("fileName", fileName);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("result", "fail");
            request.setAttribute("fileName", fileName);
        }

        return "addProfileImg.jsp";
    }
}
