<%@ page contentType="application/json; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

  {
    "postTitle": "<c:out value='${post.postTitle}'/>",
    "postImage": "<c:out value='${post.postImage}'/>",
    "postDate": "<c:out value='${post.postDate}'/>",
    "nickName": "<c:out value='${post.nickName}'/>",
    "likeCount": "<c:out value='${post.likeCount}'/>",
    "bookmarkCount": "<c:out value='${post.bookmarkCount}'/>",
    "memberId": "<c:out value='${post.memberId}'/>",
    "postContents": "<c:out value='${post.postContents}'/>",
    "categoryName": "<c:out value='${post.categoryName}'/>",
    "badgeImage": "<c:out value='${post.badgeImage}'/>",
    "memberImage": "<c:out value='${post.memberImage}'/>",
    
    "tagList": [
    <c:forEach var="tag" items="${tagList}" varStatus="status">
      "${tag}"<c:if test="${!status.last}">,</c:if>
    </c:forEach>
  ],
    "commentList": [
	  <c:forEach var="c" items="${commentList}" varStatus="status">
    {
      "commentContents": "<c:out value='${c.commentContents}'/>",
      "commentDate": "<c:out value='${c.commentDate}'/>",
      "nickName": "<c:out value='${c.nickName}'/>",
      "memberImage": "<c:out value='${c.memberImage}'/>",
      "badgeImage": "<c:out value='${c.badgeImage}'/>"
    }<c:if test="${!status.last}">,</c:if>
  </c:forEach>
]
    
  }

    