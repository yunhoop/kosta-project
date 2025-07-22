package com.camp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

// 역할 : 사용자에게 전달받은 요청사항 처리 결과를 url로 전달
public interface Action {
   String execute(HttpServletRequest request) 
         throws ServletException, IOException;
   

}