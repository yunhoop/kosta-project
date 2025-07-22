package com.camp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

public class CommentsDAO {

    // ��� ��� ��ȸ
    public List<CommentsVO> getDetailComments(SqlSession session, int postId) {
        return session.selectList("commentsMapper.getComments", postId);
    }
    
    //��� ��ȸ ���������̼�
    public List<CommentsVO> getCommentsByPagination(SqlSession session, int postId, int startRow, int endRow) {
    	Map<String, Integer> map = new HashMap<>();
    	map.put("postId", postId);
    	map.put("startRow", startRow);
    	map.put("endRow", endRow);
    	return session.selectList("commentsMapper.getCommentsPagination", map);
    }

    // ��� ���
    public boolean addComments(SqlSession session, CommentsVO vo) {
        boolean result = false;
        try {
            session.insert("commentsMapper.addComments", vo);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("��� ��� �� ���� �߻�: " + e.getMessage());
        }
        return result;
    }

    // ��� �ۼ����� ������ �̹��� ��������
    public String getMemberImage(SqlSession session, String memberId) {
        return session.selectOne("commentsMapper.getMemberImage", memberId);
    }
}
