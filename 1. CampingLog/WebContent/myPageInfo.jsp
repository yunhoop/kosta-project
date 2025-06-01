<%@ page contentType="application/json; charset=UTF-8" %>
<%@ page import="com.camp.model.MemberVO" %>
<%
   System.out.println(">> myPageInfo.jsp 도달함!");
    MemberVO member = (MemberVO) request.getAttribute("memberInfo");
    if (member == null) {
        System.out.println(">> memberInfo가 null입니다.");
    }
%>
{
  "memberId": "<%= member.getMemberId() %>",
  "email": "<%= member.getEmail() %>",
  "nickName": "<%= member.getNickName() %>",
  "name": "<%= member.getName() %>",
  "phoneNumber": "<%= member.getPhoneNumber() %>",
  "memberImage": "<%= member.getMemberImage() %>",
  "badgeImage": "<%= member.getBadgeImage() %>",
  "inDate": "<%= member.getInDate() %>",
  "postCount": <%= member.getPostCount() %>,
  "likeCount": <%= member.getLikeCount() %>
}
