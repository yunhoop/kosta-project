<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.camp.model.TagVO" %>
<%@ page import="java.util.List" %>

<%
    List<TagVO> tagList = (List<TagVO>) request.getAttribute("result");
    if (tagList == null) tagList = java.util.Collections.emptyList();
%>

[
<c:forEach var="tag" items="${result}" varStatus="status">
  {
    "tagId": "${tag.tagId}",
    "tagName": "${tag.tagName}"
  }<c:if test="${!status.last}">,</c:if>
</c:forEach>
]
