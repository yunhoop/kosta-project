package com.oopsw.memberservice.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
	MemberEntity findByMemberId(String memberId);

	MemberEntity findByEmail(String email);

	MemberEntity findByNickname(String nickname);

	void deleteByMemberId(String memberId);

	List<MemberEntity> findByGoalAndBirthdayBetweenAndHeightBetweenAndWeightBetween
		(String goal, Date birthStart, Date birthEnd, int heightMin, int heightMax, int weightMin, int weightMax);
}
