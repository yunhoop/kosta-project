<%@ page contentType="application/json; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

{ "categoryList": [
<c:forEach var="c" items="${categoryList}" varStatus="st">
    {
      "categoryId": ${c.categoryId},
      "categoryName": "${c.categoryName}"
    }<c:if test="${!st.last}">,</c:if>
</c:forEach>
] } 
