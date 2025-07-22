<%@ page contentType="application/json; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

{
  "postList": [
    <c:forEach var="post" items="${postList}" varStatus="st">
      {
        "postId": ${post.postId},
        "postTitle": "${post.postTitle}",
        "postContents": "${post.postContents}",
        "postImage": "${post.postImage}",
        "postDate": "${post.postDate}",
        "memberId": "${post.memberId}",
        "categoryId": ${post.categoryId},
        "categoryName": "${post.categoryName}",
        "likeCount": ${post.likeCount},
        "commentCount": ${post.commentCount},
        "tagList": "<c:out value='${post.tagList}'/>"
      }<c:if test="${!st.last}">,</c:if>
    </c:forEach>
  ],
  "totalPostCount": ${totalPostCount},
  "currentPage": ${currentPage}
}