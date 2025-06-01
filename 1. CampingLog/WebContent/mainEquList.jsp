<%@page import="com.camp.model.PostVO"%>
<%@page import="java.util.Collection"%>
<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    Collection<PostVO> equList = (Collection<PostVO>) request.getAttribute("equList");
%>
[
<c:forEach var="post" items="${equList}" varStatus="status">
  {
    "postId": "${post.postId}",
    "postTitle": "${post.postTitle}",
    "postImage": "${post.postImage}",
    "postDate": "${post.postDate}",
    "nickName": "${post.nickName}",
    "likeCount": "${post.likeCount}"
  }<c:if test="${!status.last}">,</c:if>
</c:forEach>
]
