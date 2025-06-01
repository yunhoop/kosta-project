<%@ page contentType="application/json; charset=UTF-8" %>
<%
    String result = (String) request.getAttribute("result");
    String fileName = (String) request.getAttribute("fileName");
%>
{
  "result": "<%= result %>",
  "fileName": "<%= fileName %>"
}
