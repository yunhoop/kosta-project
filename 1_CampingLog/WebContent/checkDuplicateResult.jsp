<%@ page contentType="application/json; charset=UTF-8" %>
<%
    Boolean isDuplicate = (Boolean) request.getAttribute("isDuplicate");
    boolean exists = (isDuplicate != null && isDuplicate);
%>
{ "exists": <%= exists %> }
