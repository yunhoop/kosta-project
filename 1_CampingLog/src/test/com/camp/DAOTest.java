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
   public void 단위_테스트_시작() {
      factory = DBCP.getSqlSessionFactory();
      service = new DetailService(factory);
      pdao=new PostDAO();
      cdao=new CommentsDAO();
      tdao=new TagDAO();
      mdao=new MemberDAO();
      session = DBCP.getSqlSessionFactory().openSession();
 
   }
   @Test
   public void getAllTags_테스트() {
	   assertEquals(35, tdao.getAllTags(session).size());
   }
   @Test
   public void getCommentCount_테스트() {
	   assertEquals(1, pdao.getCommentCount(session, 1));
   }

   @Test
   public void getCommentByPagination_테스트() {
	   assertEquals(1, cdao.getCommentsByPagination(session, 1, 1, 5).size());
   }
   
   @Test
   public void getMemberImage_테스트() {
	   assertEquals("yuqi.jpg", cdao.getMemberImage(session, "yuqi0923"));
   }
   
 
   
   @Test
   public void testGetPostById() {

      int testPostId = 1; 
      PostVO post = pdao.getPostById(session, testPostId);

      assertNotNull("게시글이 null이면 안됨", post);
      assertEquals("조회된 postId가 일치해야 함", testPostId, post.getPostId());

      System.out.println("게시글 제목: " + post.getPostTitle());
      session.close();
   }

   @Test
   public void testGetTagIdsByPostId() {
      SqlSession session = DBCP.getSqlSessionFactory().openSession();
      PostDAO pdao = new PostDAO();

      int testPostId = 1; 
      List<Integer> tagIds = pdao.getTagIdsByPostId(session, testPostId);

      assertNotNull("태그 ID 리스트가 null이면 안됨", tagIds);

      if (!tagIds.isEmpty()) {
         for (Integer tagId : tagIds) {
            assertNotNull("각 태그 ID는 null이면 안됨", tagId);
         }
      } else {
         System.out.println("이 게시글은 연결된 태그가 없습니다.");
      }

      session.close();
   }

}
