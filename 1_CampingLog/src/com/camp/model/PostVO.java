package com.camp.model;

public class PostVO {
	private int postId;
	private String postTitle;
	private String postContents;
	private String postImage;     
	private String postDate;
	private String memberId;      
	private int categoryId;
	private String nickName;
	private String memberImage;
	private String badgeImage;
	private int likeCount;
	private int bookmarkCount;
	private String categoryName;
	private int commentCount;
	private String tagList;

	public PostVO(){}

	public PostVO(String nickName, String memberImage, String badgeImage) {
		this.nickName = nickName;
		this.memberImage = memberImage;
		this.badgeImage = badgeImage;
	}

	public PostVO(String postTitle, String postContents, String postImage, String memberId, int categoryId) {
		this.postTitle = postTitle;
		this.postContents = postContents;
		this.postImage = postImage;
		this.memberId = memberId;
		this.categoryId = categoryId;
	}

	public PostVO(int postId, String postTitle, String postContents, String postImage, String postDate, String memberId,
			int categoryId) {
		this.postId = postId;
		this.postTitle = postTitle;
		this.postContents = postContents;
		this.postImage = postImage;
		this.postDate = postDate;
		this.memberId = memberId;
		this.categoryId = categoryId;
	}


	public PostVO(int postId, String postTitle, String postContents, String postImage, String postDate, String memberId,
			int categoryId, String nickName, String memberImage, String badgeImage) {
		this.postId = postId;
		this.postTitle = postTitle;
		this.postContents = postContents;
		this.postImage = postImage;
		this.postDate = postDate;
		this.memberId = memberId;
		this.categoryId = categoryId;
		this.nickName = nickName;
		this.memberImage = memberImage;
		this.badgeImage = badgeImage;
	}

	public PostVO(int postId, String title, String contents, String image, String memberId, int categoryId) {
		this.postId = postId;
		this.postTitle = title;
		this.postContents = contents;
		this.postImage = image;
		this.memberId = memberId;
		this.categoryId = categoryId;
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public String getPostTitle() {
		return postTitle;
	}

	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}

	public String getPostContents() {
		return postContents;
	}

	public void setPostContents(String postContents) {
		this.postContents = postContents;
	}

	public String getPostImage() {
		return postImage;
	}

	public void setPostImage(String postImage) {
		this.postImage = postImage;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMemberImage() {
		return memberImage;
	}

	public void setMemberImage(String memberImage) {
		this.memberImage = memberImage;
	}

	public String getBadgeImage() {
		return badgeImage;
	}

	public void setBadgeImage(String badgeImage) {
		this.badgeImage = badgeImage;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getBookmarkCount() {
		return bookmarkCount;
	}

	public void setBookmarkCount(int bookmarkCount) {
		this.bookmarkCount = bookmarkCount;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}



	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public String getTagList() {
		return tagList;
	}

	public void setTagList(String tagList) {
		this.tagList= tagList;
	}

	@Override
	public String toString() {
		return "PostVO [postId=" + postId + ", postTitle=" + postTitle + ", postContents=" + postContents
				+ ", postImage=" + postImage + ", postDate=" + postDate + ", memberId=" + memberId + ", categoryId="
				+ categoryId + ", nickName=" + nickName + ", memberImage=" + memberImage + ", badgeImage=" + badgeImage
				+ ", likeCount=" + likeCount + ", bookmarkCount=" + bookmarkCount + ", categoryName=" + categoryName
				+ ", commentCount=" + commentCount + ", tagList=" + tagList + "]";
	}
}

