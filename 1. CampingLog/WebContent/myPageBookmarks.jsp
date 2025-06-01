<%@ page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
  <c:when test="${not empty error}">
    {"error":"${error}"}
  </c:when>
  <c:otherwise>
    [
    <c:forEach var="b" items="${bookmarks}" varStatus="st">
      {
        "postId":"<c:out value='${b.postId}'/>",
        "postTitle":"<c:out value='${b.postTitle}'/>",
        "postImage":"<c:out value='${b.postImage}'/>"
      }<c:if test="${!st.last}">,</c:if>
    </c:forEach>
    ]
  </c:otherwise>
</c:choose>
