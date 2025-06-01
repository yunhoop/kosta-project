package test.com.camp;

import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.camp.model.DBCP;
import com.camp.model.PostDAO;

public class PostDAOTest {

	PostDAO pdao=null;
	
	@Before
	public void 단위_테스트_시작() throws Exception {
		pdao=new PostDAO();
	}
	
	
	@Test
	public void 전체_게시글_페이징_조회() {
		
	}
	
	@Test
	public void 카테고리별_페이징_조회() {
		
	}

}
