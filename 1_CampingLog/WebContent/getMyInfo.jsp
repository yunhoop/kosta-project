<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
  <c:when test="${empty memberInfo}">
    { "error": "unauthorized" }
  </c:when>
  <c:otherwise>
    {
      "memberId":   "${memberInfo.memberId}",
      "email":      "${memberInfo.email}",
      "nickName":   "${memberInfo.nickName}",
      "name":       "${memberInfo.name}",
      "phoneNumber":"${memberInfo.phoneNumber}",
      "memberImage":"${memberInfo.memberImage}",
      "badgeImage": "${memberInfo.badgeImage}",
      "inDate":     "${memberInfo.inDate}",
      "postCount":  ${memberInfo.postCount},
      "likeCount":  ${memberInfo.likeCount}
    }
  </c:otherwise>
</c:choose>
