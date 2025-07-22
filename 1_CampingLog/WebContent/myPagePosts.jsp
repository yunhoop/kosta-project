<%@ page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
  <c:when test="${not empty error}">
    {"error":"${error}"}
  </c:when>
  <c:otherwise>
    [
    <c:forEach var="post" items="${posts}" varStatus="st">
      {
        "postId":"<c:out value='${post.postId}'/>",
        "postTitle":"<c:out value='${post.postTitle}'/>",
        "postImage":"<c:out value='${post.postImage}'/>"
      }<c:if test="${!st.last}">,</c:if>
    </c:forEach>
    ]
  </c:otherwise>
</c:choose>
