package com.camp.model;

public class NoticeVO {
	private int noticeId;
	private String noticeName;
	private String noticeContents;
	
	
	public NoticeVO(){}
	
	public NoticeVO(int noticeId, String noticeName, String noticeContents) {
		this.noticeId = noticeId;
		this.noticeName = noticeName;
		this.noticeContents = noticeContents;
	}
	public int getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(int noticeId) {
		this.noticeId = noticeId;
	}
	public String getNoticeName() {
		return noticeName;
	}
	public void setNoticeName(String noticeName) {
		this.noticeName = noticeName;
	}
	public String getNoticeContents() {
		return noticeContents;
	}
	public void setNoticeContents(String noticeContents) {
		this.noticeContents = noticeContents;
	}
	@Override
	public String toString() {
		return "NoticeVO [noticeId=" + noticeId + ", noticeName=" + noticeName + ", noticeContents=" + noticeContents
				+ "]";
	}
	
	
	
}
