package com.oopsw.exerciseservice.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
	// private String email;
	// private String name;
	// private String nickname;
	// private String gender;
	// private Date birthday;
	// private Float height;
	private Float weight;
	// private String goal;
	// private Float bmr;
	private String memberId;
}
