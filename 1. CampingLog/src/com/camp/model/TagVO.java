package com.camp.model;

public class TagVO {
	private int tagId;
	private String tagName;
	
	public TagVO(){}
	
	public TagVO(int tagId, String tagName) {
		this.tagId = tagId;
		this.tagName = tagName;
	}
	public int getTagId() {
		return tagId;
	}
	public void setTagId(int tagId) {
		this.tagId = tagId;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	@Override
	public String toString() {
		return "TagVO [tagId=" + tagId + ", tagName=" + tagName + "]";
	}
	
	
	

}

