package com.oopsw.memberservice.jpa;

import java.util.Date;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
public class MemberEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "member_id", unique = true, nullable = false)
	private String memberId;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "pw")
	private String pw;

	@Column(name = "name")
	private String name;

	@Column(name = "nickname", unique = true)
	private String nickname;

	@Column(name = "gender")
	private String gender;

	@Column(name = "birthday")
	private Date birthday;

	@Column(name = "height")
	private Float height;

	@Column(name = "weight")
	private Float weight;

	@Column(name = "goal")
	private String goal;

	@Column(name = "bmr")
	private Float bmr;

	@Column(name = "join_date", nullable = false, updatable = false, insertable = false)
	@ColumnDefault(value = "CURRENT_TIMESTAMP")
	private Date joinDate;

	@Column(name = "member_type", length = 30)
	private String memberType;

	@Column(name = "profile_img")
	private String profileImg;

}
