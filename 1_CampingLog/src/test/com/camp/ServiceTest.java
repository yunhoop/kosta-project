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
	public void ����_�׽�Ʈ_����() throws Exception {
		SqlSessionFactory factory=DBCP.getSqlSessionFactory();
		ds=new DetailService(factory);
	}


	@Test
	public void ����_�Խñ�_��ȸ_����() {
		assertEquals(1, ds.getPostDetails(1).getPostId());
	}
	
	@Test
	public void ����_�Խñ�_��ȸ_����() {
		assertNotEquals(2, ds.getPostDetails(1).getPostId());
	}
	
	@Test
	public void ����_�Խñ�_���_��ȸ_����() {
		assertEquals(1, ds.getCommentsForPost(1).size());
	}
	
	@Test
	public void ����_�Խñ�_���_��ȸ_����() {
		assertNotEquals(52, ds.getCommentsForPost(1).size());
	}
}
