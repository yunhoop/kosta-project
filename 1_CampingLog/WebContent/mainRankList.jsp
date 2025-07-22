<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.camp.model.MemberVO" %>
<%@ page import="java.util.Collection" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="java.util.HashMap" %>

<%
Collection<MemberVO> rankList = (Collection<MemberVO>) request.getAttribute("rankList");
String rankDate = (String) request.getAttribute("rankDate");

if (rankList == null) {
    rankList = new java.util.ArrayList<MemberVO>();
}

if (rankDate == null) {
    rankDate = "기준일 없음";
}

HashMap<String, Object> result = new HashMap<>();
result.put("rankDate", rankDate);
result.put("rankList", rankList);

Gson gson = new Gson();
String json = gson.toJson(result);
out.print(json);
%>
