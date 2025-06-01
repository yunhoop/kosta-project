package com.camp.model;

import org.apache.ibatis.session.SqlSession;

public class LoginDAO {

    public MemberVO login(MemberVO vo) {
        MemberVO member = null;  // 로그인한 사용자 정보를 담을 객체
        SqlSession session = DBCP.getSqlSessionFactory().openSession();

        try {
            // 로그인한 사용자 정보를 전체 조회
            member = session.selectOne("memberMapper.login", vo);
        } finally {
            session.close();
        }

        return member; // 닉네임뿐만 아니라 전체 MemberVO 객체를 반환
    }
}
