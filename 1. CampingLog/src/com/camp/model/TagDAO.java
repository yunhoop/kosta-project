package com.camp.model;

import java.util.List;
import org.apache.ibatis.session.SqlSession;

import com.camp.model.DBCP;
import com.camp.model.TagVO;

public class TagDAO {

    public List<String> getDetailTag(SqlSession session, int postId) {
        return session.selectList("tagMapper.getTag", postId);
    }
    
    public List<TagVO> getTagsByCategory(int categoryId) {
		SqlSession session = DBCP.getSqlSessionFactory().openSession();
		List<TagVO> list = session.selectList("tagMapper.getTagsByCategory", categoryId);
		session.close();
		return list;
	}
	public List<TagVO> getAllTags(SqlSession session) {
		return session.selectList("tagMapper.getAllTags");
	}
	public List<TagVO> getTagsByCategoryId(SqlSession session, int categoryId) {
	    return session.selectList("tagMapper.getTagsByCategoryId", categoryId);
	}
}