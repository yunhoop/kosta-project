<%@ page contentType="application/json; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

{ "tagList": [
<c:forEach var="tag" items="${tagList}" varStatus="st">
      {
        "tagId": "${tag.tagId}",
        "tagName": "${tag.tagName}"
      }<c:if test="${!st.last}">,</c:if>
</c:forEach>
] }
