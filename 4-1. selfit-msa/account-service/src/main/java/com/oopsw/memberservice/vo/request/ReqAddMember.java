package com.oopsw.memberservice.vo.request;

import java.util.Date;

import lombok.Data;

@Data
public class ReqAddMember {
	private String email;
	private String pw;
	private String name;
	private String nickname;
	private String gender;
	private Date birthday;
	private Float height;
	private Float weight;
	private String goal;
	private String memberType;
}
