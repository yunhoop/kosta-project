package com.camp.model;

public class MemberVO {
	private String memberId;
	private String pw;
	private String email;
	private String nickName;
	private String name;
	private String phoneNumber;
	private String inDate;
	private String memberImage;
	private int gradeId;
	private String badgeImage;
	private int postCount;
	private int likeCount;
	
	public MemberVO() {
		super();
	}

	public MemberVO(String memberId, String pw) {
		super();
		this.memberId = memberId;
		this.pw = pw;
	}
	
	public MemberVO(String memberId, String email, String nickName, String inDate) {
		super();
		this.memberId = memberId;
		this.email = email;
		this.nickName = nickName;
		this.inDate = inDate;
	}
	
	public MemberVO(String memberId, String pw, String email, String nickName, String name, String phoneNumber) {
		super();
		this.memberId = memberId;
		this.pw = pw;
		this.email = email;
		this.nickName = nickName;
		this.name = name;
		this.phoneNumber = phoneNumber;
	}

	public MemberVO(String memberId, String pw, String email, String nickName, String name, String phoneNumber,
			String inDate, int gradeId) {
		super();
		this.memberId = memberId;
		this.pw = pw;
		this.email = email;
		this.nickName = nickName;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.inDate = inDate;
		this.gradeId = gradeId;
	}

	public MemberVO(String memberId, String pw, String email, String nickName, String name, String phoneNumber,
			String inDate, String memberImage, int gradeId) {
		super();
		this.memberId = memberId;
		this.pw = pw;
		this.email = email;
		this.nickName = nickName;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.inDate = inDate;
		this.memberImage = memberImage;
		this.gradeId = gradeId;
	}

	public MemberVO(String memberId, String pw, String email, String nickName, String name, String phoneNumber,
			String inDate, String memberImage, int gradeId, String badgeImage) {
		super();
		this.memberId = memberId;
		this.pw = pw;
		this.email = email;
		this.nickName = nickName;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.inDate = inDate;
		this.memberImage = memberImage;
		this.gradeId = gradeId;
		this.badgeImage = badgeImage;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getInDate() {
		return inDate;
	}

	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	public String getMemberImage() {
		return memberImage;
	}

	public void setMemberImage(String memberImage) {
		this.memberImage = memberImage;
	}

	public int getGradeId() {
		return gradeId;
	}

	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}

	public String getBadgeImage() {
		return badgeImage;
	}

	public void setBadgeImage(String badgeImage) {
		this.badgeImage = badgeImage;
	}

	public int getPostCount() {
		return postCount;
	}

	public void setPostCount(int postCount) {
		this.postCount = postCount;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	@Override
	public String toString() {
		return "MemberVO [memberId=" + memberId + ", pw=" + pw + ", email=" + email + ", nickName=" + nickName
				+ ", name=" + name + ", phoneNumber=" + phoneNumber + ", inDate=" + inDate + ", memberImage="
				+ memberImage + ", gradeId=" + gradeId + ", badgeImage=" + badgeImage + ", postCount=" + postCount
				+ ", likeCount=" + likeCount + "]";
	}

	

	
	
	
}
