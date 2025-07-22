<%@page import="com.camp.model.PostVO"%>
<%@page import="java.util.Collection"%>
<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    // popularList는 반드시 setAttribute 되어 있어야 함
    Collection<PostVO> popularList = (Collection<PostVO>) request.getAttribute("popularList");
    if (popularList == null) {popularList = java.util.Collections.emptyList();}
%>
[
<c:forEach var="post" items="${popularList}" varStatus="status">
  {
    "postId": "${post.postId}",
    "postTitle": "${post.postTitle}",
    "postImage": "${post.postImage}",
    "postDate": "${post.postDate}",
    "nickName": "${post.nickName}",
    "likeCount": ${post.likeCount}
  }<c:if test="${!status.last}">,</c:if>
</c:forEach>
]
