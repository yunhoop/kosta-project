package com.camp.model;

import org.apache.ibatis.session.SqlSession;

public class LoginDAO {

    public MemberVO login(MemberVO vo) {
        MemberVO member = null;  // �α����� ����� ������ ���� ��ü
        SqlSession session = DBCP.getSqlSessionFactory().openSession();

        try {
            // �α����� ����� ������ ��ü ��ȸ
            member = session.selectOne("memberMapper.login", vo);
        } finally {
            session.close();
        }

        return member; // �г��ӻӸ� �ƴ϶� ��ü MemberVO ��ü�� ��ȯ
    }
}
