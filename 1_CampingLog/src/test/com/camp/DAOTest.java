package test.com.camp;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import com.camp.model.CommentsDAO;
import com.camp.model.DBCP;
import com.camp.model.MemberDAO;
import com.camp.model.MemberVO;
import com.camp.model.PostDAO;
import com.camp.model.PostVO;
import com.camp.model.TagDAO;
import com.camp.service.DetailService;

public class DAOTest {
   SqlSessionFactory factory=null;
   DetailService service=null;
   PostDAO pdao=null;
   CommentsDAO cdao=null;
   TagDAO tdao=null;
   MemberDAO mdao=null;
   SqlSession session=null;
   
   @Before
   public void ����_�׽�Ʈ_����() {
      factory = DBCP.getSqlSessionFactory();
      service = new DetailService(factory);
      pdao=new PostDAO();
      cdao=new CommentsDAO();
      tdao=new TagDAO();
      mdao=new MemberDAO();
      session = DBCP.getSqlSessionFactory().openSession();
 
   }
   @Test
   public void getAllTags_�׽�Ʈ() {
	   assertEquals(35, tdao.getAllTags(session).size());
   }
   @Test
   public void getCommentCount_�׽�Ʈ() {
	   assertEquals(1, pdao.getCommentCount(session, 1));
   }

   @Test
   public void getCommentByPagination_�׽�Ʈ() {
	   assertEquals(1, cdao.getCommentsByPagination(session, 1, 1, 5).size());
   }
   
   @Test
   public void getMemberImage_�׽�Ʈ() {
	   assertEquals("yuqi.jpg", cdao.getMemberImage(session, "yuqi0923"));
   }
   
 
   
   @Test
   public void testGetPostById() {

      int testPostId = 1; 
      PostVO post = pdao.getPostById(session, testPostId);

      assertNotNull("�Խñ��� null�̸� �ȵ�", post);
      assertEquals("��ȸ�� postId�� ��ġ�ؾ� ��", testPostId, post.getPostId());

      System.out.println("�Խñ� ����: " + post.getPostTitle());
      session.close();
   }

   @Test
   public void testGetTagIdsByPostId() {
      SqlSession session = DBCP.getSqlSessionFactory().openSession();
      PostDAO pdao = new PostDAO();

      int testPostId = 1; 
      List<Integer> tagIds = pdao.getTagIdsByPostId(session, testPostId);

      assertNotNull("�±� ID ����Ʈ�� null�̸� �ȵ�", tagIds);

      if (!tagIds.isEmpty()) {
         for (Integer tagId : tagIds) {
            assertNotNull("�� �±� ID�� null�̸� �ȵ�", tagId);
         }
      } else {
         System.out.println("�� �Խñ��� ����� �±װ� �����ϴ�.");
      }

      session.close();
   }

}
