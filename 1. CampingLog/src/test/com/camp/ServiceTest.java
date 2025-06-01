package test.com.camp;

import static org.junit.Assert.*;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.camp.model.DBCP;
import com.camp.service.DetailService;

public class ServiceTest {
	DetailService ds=null;	
	
	@Before
	public void 단위_테스트_시작() throws Exception {
		SqlSessionFactory factory=DBCP.getSqlSessionFactory();
		ds=new DetailService(factory);
	}


	@Test
	public void 서비스_게시글_조회_성공() {
		assertEquals(1, ds.getPostDetails(1).getPostId());
	}
	
	@Test
	public void 서비스_게시글_조회_실패() {
		assertNotEquals(2, ds.getPostDetails(1).getPostId());
	}
	
	@Test
	public void 서비스_게시글_댓글_조회_성공() {
		assertEquals(1, ds.getCommentsForPost(1).size());
	}
	
	@Test
	public void 서비스_게시글_댓글_조회_실패() {
		assertNotEquals(52, ds.getCommentsForPost(1).size());
	}
}
