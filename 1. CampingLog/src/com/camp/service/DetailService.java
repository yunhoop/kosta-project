package com.camp.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.camp.model.CommentsDAO;
import com.camp.model.CommentsVO;
import com.camp.model.PostDAO;
import com.camp.model.PostVO;
import com.camp.model.TagDAO;

public class DetailService {

    private SqlSessionFactory factory;
    private PostDAO postDAO;
    private CommentsDAO commentsDAO;
    private TagDAO tagDAO;

    public DetailService(SqlSessionFactory factory) {
        this.factory = factory;
        postDAO = new PostDAO();
        commentsDAO = new CommentsDAO();
        tagDAO = new TagDAO();
    }

    public PostVO getPostDetails(int postId) {
        try (SqlSession session = factory.openSession()) {
            return postDAO.getDetailContents(session, postId);
        }
    }

    public List<String> getTagsForPost(int postId) {
        try (SqlSession session = factory.openSession()) {
            return tagDAO.getDetailTag(session, postId);
        }
    }

    public List<CommentsVO> getCommentsForPost(int postId) {
        try (SqlSession session = factory.openSession()) {
            return commentsDAO.getDetailComments(session, postId);
        }
    }
    
    public List<CommentsVO> getCommentsByPagination(int postId, int startRow, int endRow) {
    	try (SqlSession session = factory.openSession()) {
    		List<CommentsVO> vo = commentsDAO.getCommentsByPagination(session, postId, startRow, endRow);
    		
    		return vo;
    	}
    }
    
    public int getCommentCount(int postId) {
    	try (SqlSession session = factory.openSession()) {
    		return postDAO.getCommentCount(session, postId);
    	}
    }

    public boolean addComment(CommentsVO comment) {
        boolean result = false;
        try (SqlSession session = factory.openSession()) {
            result = commentsDAO.addComments(session, comment);
            if (result) session.commit();
            else session.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getMemberImage(String memberId) {
        try (SqlSession session = factory.openSession()) {
            return postDAO.getMemberImage(session, memberId);
        }
    }
    
    public boolean InsertBookmark(int postId, String memberId){
    	boolean result = false;
    	try (SqlSession session = factory.openSession()) {
    		result = postDAO.InsertBookmark(session, postId, memberId);
    		
    		if (result) session.commit();
    		else session.rollback();
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
    	return result;
    }
}