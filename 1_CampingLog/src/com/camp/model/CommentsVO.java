package com.camp.model;

public class CommentsVO {
	private int commentId;
	private String commentContents;
	private String commentDate;
	private int postId;
	private String memberId;
	private String nickName;
	private String badgeImage;
	private String memberImage;
	
	
	public CommentsVO(){}
	
	
	
	public CommentsVO(String commentContents, int postId, String memberId) {
		super();
		this.postId = postId;
		this.memberId = memberId;
		this.commentContents = commentContents;
	}
	
	
	public CommentsVO(int commentId, String commentContents, String commentDate, int postId, String memberId,
			String nickName, String badgeImage, String memberImage) {
		super();
		this.commentId = commentId;
		this.commentContents = commentContents;
		this.commentDate = commentDate;
		this.postId = postId;
		this.memberId = memberId;
		this.nickName = nickName;
		this.badgeImage = badgeImage;
		this.memberImage = memberImage;
	}



	public int getCommentId() {
		return commentId;
	}



	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}



	public String getCommentContents() {
		return commentContents;
	}



	public void setCommentContents(String commentContents) {
		this.commentContents = commentContents;
	}



	public String getCommentDate() {
		return commentDate;
	}



	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}



	public int getPostId() {
		return postId;
	}



	public void setPostId(int postId) {
		this.postId = postId;
	}



	public String getMemberId() {
		return memberId;
	}



	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}



	public String getNickName() {
		return nickName;
	}



	public void setNickName(String nickName) {
		this.nickName = nickName;
	}



	public String getBadgeImage() {
		return badgeImage;
	}



	public void setBadgeImage(String badgeImage) {
		this.badgeImage = badgeImage;
	}



	public String getMemberImage() {
		return memberImage;
	}



	public void setMemberImage(String memberImage) {
		this.memberImage = memberImage;
	}



	@Override
	public String toString() {
		return "CommentsVO [commentId=" + commentId + ", commentContents=" + commentContents + ", commentDate="
				+ commentDate + ", postId=" + postId + ", memberId=" + memberId + ", nickName=" + nickName
				+ ", badgeImage=" + badgeImage + ", memberImage=" + memberImage + "]";
	}

	
}