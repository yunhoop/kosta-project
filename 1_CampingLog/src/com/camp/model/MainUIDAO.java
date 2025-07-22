package com.camp.model;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

public class MainUIDAO {
	/* 인기 상승 캠핑장 상위 3개*/
	public Collection<PostVO> getPopularCampingList(int categoryId) {
		SqlSession conn = DBCP.getSqlSessionFactory().openSession();
		List<PostVO> list = conn.selectList("mainUIMapper.getPopularCampingList", categoryId);
		conn.close();
		return list;
	}
	//상위3개 상세조회
	
	
	
	/* 명예의 캠핑로거 상위 5개*/
	public Collection<MemberVO> getTopMembersByLikes(int i) {
	    SqlSession conn = DBCP.getSqlSessionFactory().openSession();
	    List<MemberVO> List = null;
	    List = conn.selectList("mainUIMapper.getTopMemberRank", 5);
	    conn.close();
	    return List;
	}
	//상위5명 프로필 상세조회

	
	/* 인기 장비리뷰 상위 3개*/
	public Collection<PostVO> getPopularEquList(int categoryId) {
		SqlSession conn = DBCP.getSqlSessionFactory().openSession();
		List<PostVO> list = conn.selectList("mainUIMapper.getPopularEquList", categoryId);
		conn.close();
		return list;
	}
	
	//상위3개 상세조회
	
	
	
	
	/* 실시간 좋아요 회원 승급*/
	public boolean updateMemberGradeById(String memberId) {
	    try (SqlSession session = DBCP.getSqlSessionFactory().openSession(true)) {
	        int updated = session.update("mainUIMapper.updateMemberGradeById", memberId);
	        System.out.println("memberId=" + memberId + " 등급 갱신 결과: " + updated + "건");
	        return updated > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	
	/* 홈화면 회원 승급*/
	public boolean updateAllMemberGrade() {
	    try (SqlSession session = DBCP.getSqlSessionFactory().openSession(true)) {
	        int updatedRows = session.update("mainUIMapper.updateAllMemberGrade");
	        System.out.println("등급 갱신된 회원 수: " + updatedRows);
	        return updatedRows > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	/* 홈화면 어제 날짜 기준으로 좋아요 수 TOP 5 조회 */
	public Collection<MemberVO> getYesterdayTopMembersByLikes(String date) {
	    SqlSession conn = DBCP.getSqlSessionFactory().openSession();
	    List<MemberVO> list = conn.selectList("mainUIMapper.getYesterdayTopMemberRank", date);
	    conn.close();
	    return list;
	}




	
}
