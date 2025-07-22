<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

{
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
  ],
  "commentCount": "<c:out value='${commentCount}'/>",
  "currentPage": "<c:out value='${currentPage}'/>"
}