package com.camp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import com.camp.model.DBCP;
import com.camp.model.PostDAO;
import com.camp.model.PostVO;
import com.camp.model.TagDAO;
import com.camp.model.TagVO;

public class PostService {
	private PostDAO dao = new PostDAO();

	public boolean registerPostWithTags(PostVO vo, String[] tagIds) {
		SqlSession session = DBCP.getSqlSessionFactory().openSession();
		try {
			int r = dao.insertPost(session, vo);
			if (r == 0) {
				session.rollback();
				return false;
			}

			int postId = dao.getLastPostId(session); 

			if (tagIds != null) {
				for (String tagIdStr : tagIds) {
					int tagId = Integer.parseInt(tagIdStr);
					dao.insertPostTag(session, postId, tagId);
				}
			}

			session.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
			return false;
		} finally {
			session.close();
		}
	}

	public Map<String, Object> getPostDetail(int postId) {
		SqlSession session = DBCP.getSqlSessionFactory().openSession();
		Map<String, Object> result = new HashMap<>();
		try {
			PostDAO postDao = new PostDAO();
			TagDAO tagDao = new TagDAO();

			PostVO post = postDao.getPostById(session, postId);
			System.out.println("post: " + post);

			result.put("post", post);


			List<TagVO> allTags = tagDao.getTagsByCategoryId(session, post.getCategoryId());
			result.put("allTags", allTags);


			List<Integer> selectedTagIds = postDao.getTagIdsByPostId(session, postId);
			result.put("selectedTags", selectedTagIds);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	public boolean updatePostWithTags(PostVO vo, String[] tagIds) {
		SqlSession session = DBCP.getSqlSessionFactory().openSession();
		try {
			int r = dao.updatePost(session, vo);
			if (r == 0) {
				session.rollback();
				return false;
			}

			dao.deleteTagsByPostId(session, vo.getPostId());

			if (tagIds != null) {
				for (String tagIdStr : tagIds) {
					int tagId = Integer.parseInt(tagIdStr);
					dao.insertPostTag(session, vo.getPostId(), tagId);
				}
			}

			session.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
			return false;
		} finally {
			session.close();
		}
	}

	public boolean deletePostWithTags(int postId) {
		SqlSession session = DBCP.getSqlSessionFactory().openSession();
		try {

			dao.deleteTagsByPostId(session, postId); 

			int r = dao.deletePost(session, postId);

			session.commit();
			return r > 0;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
			return false;
		} finally {
			session.close();
		}
	}

}








