<%@ page contentType="application/json; charset=UTF-8" %>
<%
  String result = (String) request.getAttribute("result");
  String nickname = (String) request.getAttribute("nickname");
%>
{
  "result": "<%= result %>",
  "nickname": "<%= nickname != null ? nickname : "" %>"
}
