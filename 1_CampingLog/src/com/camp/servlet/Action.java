package com.camp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

// ���� : ����ڿ��� ���޹��� ��û���� ó�� ����� url�� ����
public interface Action {
   String execute(HttpServletRequest request) 
         throws ServletException, IOException;
   

}