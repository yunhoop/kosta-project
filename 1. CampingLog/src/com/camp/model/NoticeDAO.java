package com.camp.model;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

public class NoticeDAO {
	public List<NoticeVO> getNoticeList() {
        SqlSession session = DBCP.getSqlSessionFactory().openSession();
        List<NoticeVO> list = session.selectList("noticeMapper.getNoticeList");
        session.close();
        return list;
    }

}
