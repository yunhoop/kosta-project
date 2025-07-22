package com.camp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.camp.model.PostVO;

public class PostDAO {
	
	// 전체 게시글 페이징 조회
	public List<PostVO> getPostPage(int start, int end, String searchTerm) {
		SqlSession conn = DBCP.getSqlSessionFactory().openSession();
		Map<Object, Object> map = new HashMap<>();
		map.put("start", start);
		map.put("end", end);
		map.put("searchTerm", searchTerm);
		List<PostVO> list = conn.selectList("postMapper.getPostsByPage", map);
		conn.close();
		return list;
	}
	
	// 카테고리별 페이징 조회 
	public List<PostVO> getPostPageByCategory(int categoryId, String tagList , int start, int end) {
		try (SqlSession conn = DBCP.getSqlSessionFactory().openSession()) {
			Map<Object, Object> map = new HashMap<>();
			map.put("categoryId", categoryId);
			map.put("tagList", tagList);
			map.put("start",      start);
			map.put("end",        end);
			return conn.selectList("postMapper.getPostsByCategoryPage", map);
		}
	}
	
	// 카테고리별 총 게시글 수 조회 
	public int getTotalPostCountByCategory(int categoryId, String tagList) {
		try (SqlSession conn = DBCP.getSqlSessionFactory().openSession()) {
			Map<Object, Object> map = new HashMap<>();
			map.put("categoryId", categoryId);
			map.put("tagList",     tagList != null ? tagList : "");
			return conn.selectOne("postMapper.getTotalPostCountByCategory", map);
		}
	}
	
	// 전체 게시글 수 가져오기
	public int getTotalPostCount(String searchTerm) {
		try (SqlSession conn = DBCP.getSqlSessionFactory().openSession()) {
			Map<String,Object> params = new HashMap<>();
			// 검색어가 null 이거나 빈 문자열이면, XML에서는 WHERE 절이 제거되어 전체 카운트
			params.put("searchTerm", searchTerm != null ? searchTerm.trim() : "");
			return conn.selectOne("postMapper.getTotalPostCount", params);
		}
	}
  //댓글 수 조회
    public int getCommentCount(SqlSession session, int postId) {
    	int count= session.selectOne("postMapper.getCommentCount", postId);
    	
    	return count;
    }
    
	//게시글 카테고리 가져오기
	public List<PostVO> getCategoryName() {
		SqlSession conn = DBCP.getSqlSessionFactory().openSession();
		List<PostVO> list = conn.selectList("postMapper.getCategoryName");
		conn.close();
		return list;
	}
    
    public PostVO getDetailContents(SqlSession session, int postId){
        return session.selectOne("postMapper.getContents", postId);
    }

    public List<String> getDetailTag(SqlSession session, int postId) {
        return session.selectList("postMapper.getTag", postId);
    }

    public String getMemberImage(SqlSession session, String memberId) {
        return session.selectOne("commentsMapper.getMemberImage", memberId);
    }
    
    public int insertPost(SqlSession session, PostVO vo) {
		return session.insert("postMapper.insertPost", vo);
	}
    
    public int getLastPostId(SqlSession session) {
		return session.selectOne("postMapper.getLastPostId");
	}
    
    public int insertPostTag(SqlSession session, int postId, int tagId) {
		Map<String, Integer> map = new HashMap<>();
		map.put("postId", postId);
		map.put("tagId", tagId);
		return session.insert("postTagMapper.insertPostTag", map);
	}
    
    public PostVO getPostById(SqlSession session, int postId) {
		return session.selectOne("postMapper.getPostById", postId);
	}
	
	public List<Integer> getSelectedTagIds(SqlSession session, int postId) {
		return session.selectList("postTagMapper.getSelectedTagIds", postId);
	}

	public List<Integer> getTagIdsByPostId(SqlSession session, int postId) {
	    return session.selectList("postTagMapper.getTagIdsByPostId", postId);
	}
	
	public int updatePost(SqlSession session, PostVO vo) {
	    return session.update("postMapper.updatePost", vo);
	}

	public int deleteTagsByPostId(SqlSession session, int postId) {
	    return session.delete("postTagMapper.deleteTagsByPostId", postId);
	}
	
	public int deletePost(SqlSession session, int postId) {
	    return session.delete("postMapper.deletePost", postId);
	}
	
	/* 실시간 좋아요 추가*/
	public boolean insertLike(int postId, String memberId) {
	    boolean result = false;
		Map<String, Object> param = new HashMap<>();
	    param.put("postId", postId);
	    param.put("memberId", memberId);

	    SqlSession conn = DBCP.getSqlSessionFactory().openSession();
	    try{
	    	if(!isAlreadyLiked(conn, memberId, postId)){
	    		conn.insert("postMapper.insertLike", param);
	    		result=true;
	    	}else {
	    		result = false;
	    	}
	    }catch (Exception e){
			e.printStackTrace();
			System.out.println("좋아요 오류");
		}
	    conn.commit();
	    conn.close();
	    return result;
	}
	
	public boolean InsertBookmark(SqlSession session, int postId, String memberId) {
		boolean result = false;
		Map<String, Object> param = new HashMap<>();
		param.put("postId", postId);
		param.put("memberId", memberId);
		try{
			if(!isAlreadyBookmarked(session, memberId, postId)){
				session.insert("postMapper.insertBookmark", param);
				result = true;
			}else {
				result = false;
			}
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("북마크 오류");
		}
		
		return result;
	}
	public boolean isAlreadyBookmarked(SqlSession session, String memberId, int postId) {
		Map<String, Object> param = new HashMap<>();
		param.put("postId", postId);
		param.put("memberId", memberId);
		int count = session.selectOne("postMapper.isAlreadyBookmarked", param);
		
		return count>=1;
	}
	public boolean isAlreadyLiked(SqlSession conn, String memberId, int postId) {
		Map<String, Object> param = new HashMap<>();
		param.put("postId", postId);
		param.put("memberId", memberId);
		int count = conn.selectOne("postMapper.isAlreadyLiked", param);
		
		return count>=1;
	}

}
