package com.camp.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/controller")
public class FrontControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd=request.getParameter("cmd");
		if(cmd==null) cmd="mainUI";

		Action a=ActionFactory.getAction(cmd);

		String url=a.execute(request);

		if (url.startsWith("redirect:")) {

			response.sendRedirect(url.substring("redirect:".length()));
		} else {

			request.getRequestDispatcher("/" + url).forward(request, response);
		}
	}
}