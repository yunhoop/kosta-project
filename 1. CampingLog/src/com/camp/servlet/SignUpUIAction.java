package com.camp.servlet;

import javax.servlet.http.HttpServletRequest;

public class SignUpUIAction implements Action {
    @Override
    public String execute(HttpServletRequest request) {
        return "signup.html";
    }
}
