package com.camp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

public class CommentsDAO {

    // 댓글 목록 조회
    public List<CommentsVO> getDetailComments(SqlSession session, int postId) {
        return session.selectList("commentsMapper.getComments", postId);
    }
    
    //댓글 조회 페이지네이션
    public List<CommentsVO> getCommentsByPagination(SqlSession session, int postId, int startRow, int endRow) {
    	Map<String, Integer> map = new HashMap<>();
    	map.put("postId", postId);
    	map.put("startRow", startRow);
    	map.put("endRow", endRow);
    	return session.selectList("commentsMapper.getCommentsPagination", map);
    }

    // 댓글 등록
    public boolean addComments(SqlSession session, CommentsVO vo) {
        boolean result = false;
        try {
            session.insert("commentsMapper.addComments", vo);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("댓글 등록 중 오류 발생: " + e.getMessage());
        }
        return result;
    }

    // 댓글 작성자의 프로필 이미지 가져오기
    public String getMemberImage(SqlSession session, String memberId) {
        return session.selectOne("commentsMapper.getMemberImage", memberId);
    }
}
