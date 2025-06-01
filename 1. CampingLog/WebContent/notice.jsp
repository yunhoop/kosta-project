<%@ page contentType="application/json; charset=UTF-8" %>
<%@ page import="java.util.*, com.camp.model.NoticeVO" %>
<%
    List<NoticeVO> noticeList = (List<NoticeVO>) request.getAttribute("noticeList");
    if (noticeList == null) {
        noticeList = new ArrayList<>();
    }
%>
[
<%
    for (int i = 0; i < noticeList.size(); i++) {
        NoticeVO vo = noticeList.get(i);
%>
    {
        "noticeId": "<%= vo.getNoticeId() %>",
        "noticeName": "<%= vo.getNoticeName().replace("\"", "\\\"") %>",
        "noticeContents": "<%= vo.getNoticeContents().replace("\"", "\\\"") %>"
    }<%= (i < noticeList.size() - 1) ? "," : "" %>
<%
    }
%>
]
