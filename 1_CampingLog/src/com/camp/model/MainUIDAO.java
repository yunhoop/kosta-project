package com.camp.model;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

public class MainUIDAO {
	/* �α� ��� ķ���� ���� 3��*/
	public Collection<PostVO> getPopularCampingList(int categoryId) {
		SqlSession conn = DBCP.getSqlSessionFactory().openSession();
		List<PostVO> list = conn.selectList("mainUIMapper.getPopularCampingList", categoryId);
		conn.close();
		return list;
	}
	//����3�� ����ȸ
	
	
	
	/* ���� ķ�ηΰ� ���� 5��*/
	public Collection<MemberVO> getTopMembersByLikes(int i) {
	    SqlSession conn = DBCP.getSqlSessionFactory().openSession();
	    List<MemberVO> List = null;
	    List = conn.selectList("mainUIMapper.getTopMemberRank", 5);
	    conn.close();
	    return List;
	}
	//����5�� ������ ����ȸ

	
	/* �α� ��񸮺� ���� 3��*/
	public Collection<PostVO> getPopularEquList(int categoryId) {
		SqlSession conn = DBCP.getSqlSessionFactory().openSession();
		List<PostVO> list = conn.selectList("mainUIMapper.getPopularEquList", categoryId);
		conn.close();
		return list;
	}
	
	//����3�� ����ȸ
	
	
	
	
	/* �ǽð� ���ƿ� ȸ�� �±�*/
	public boolean updateMemberGradeById(String memberId) {
	    try (SqlSession session = DBCP.getSqlSessionFactory().openSession(true)) {
	        int updated = session.update("mainUIMapper.updateMemberGradeById", memberId);
	        System.out.println("memberId=" + memberId + " ��� ���� ���: " + updated + "��");
	        return updated > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	
	/* Ȩȭ�� ȸ�� �±�*/
	public boolean updateAllMemberGrade() {
	    try (SqlSession session = DBCP.getSqlSessionFactory().openSession(true)) {
	        int updatedRows = session.update("mainUIMapper.updateAllMemberGrade");
	        System.out.println("��� ���ŵ� ȸ�� ��: " + updatedRows);
	        return updatedRows > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	/* Ȩȭ�� ���� ��¥ �������� ���ƿ� �� TOP 5 ��ȸ */
	public Collection<MemberVO> getYesterdayTopMembersByLikes(String date) {
	    SqlSession conn = DBCP.getSqlSessionFactory().openSession();
	    List<MemberVO> list = conn.selectList("mainUIMapper.getYesterdayTopMemberRank", date);
	    conn.close();
	    return list;
	}




	
}
