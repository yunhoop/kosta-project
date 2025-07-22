<%@ page contentType="application/json; charset=UTF-8" %>
<%
    Boolean verified = (Boolean) request.getAttribute("verified");
%>
{ "verified": <%= verified %> }
