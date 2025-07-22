<%@ page contentType="application/json; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

{
  "postList": [
    <c:forEach var="post" items="${postList}" varStatus="st">
      {
        "postId": ${post.postId},
        "postTitle": "<c:out value='${post.postTitle}'/>",
        "postContents": "<c:out value='${post.postContents}'/>",
        "postImage": "<c:out value='${post.postImage}'/>",
        "postDate": "<c:out value='${post.postDate}'/>",
        "memberId": "<c:out value='${post.memberId}'/>",
        "categoryId": ${post.categoryId},
        "categoryName": "<c:out value='${post.categoryName}'/>",
        "likeCount": ${post.likeCount},
        "commentCount": ${post.commentCount},
        "tagList": "<c:out value='${post.tagList}'/>"
      }<c:if test="${!st.last}">,</c:if>
    </c:forEach>
  ],
  "totalPostCount": ${totalPostCount},
  "currentPage": ${currentPage}
}
