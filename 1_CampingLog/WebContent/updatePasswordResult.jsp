<%@ page contentType="application/json; charset=UTF-8" %>
<%
    String result = (String) request.getAttribute("result");
%>
{ "result": "<%= result %>" }
