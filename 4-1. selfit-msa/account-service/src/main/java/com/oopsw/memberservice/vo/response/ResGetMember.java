package com.oopsw.memberservice.vo.response;

import java.util.Date;

import lombok.Data;

@Data
public class ResGetMember {
	private String email;
	private String name;
	private String nickname;
	private String gender;
	private Date birthday;
	private Float height;
	private Float weight;
	private String goal;
	private String profileImg;
	private Float bmr;
}