package test.com.camp;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.camp.model.DBCP;
import com.camp.model.MemberDAO;
import com.camp.model.MemberVO;
import com.camp.model.PostDAO;
import com.camp.model.PostVO;
import com.camp.service.DetailService;
import com.camp.servlet.ActionFactory;
import com.camp.servlet.DetailAction;
import com.camp.servlet.GetCategoryNameAction;

public class ActionTest {
	
	@Test
	public void DetailTest() {
		DetailService service = new DetailService(DBCP.getSqlSessionFactory());

		PostVO post = service.getPostDetails(1); // postId=1번 게시글

		assertNotNull("게시글이 존재해야 합니다.", post);
		assertEquals("게시글 ID가 일치해야 합니다.", 1, post.getPostId());
	}
	
	@Test
    public void testGetMyInfo() {
        MemberDAO dao = new MemberDAO();
        
        MemberVO member = dao.getMyInfo("iveliz"); // memberId=1번 회원
        
        assertNotNull("회원 정보는 null이면 안 됩니다.", member);
        assertEquals("회원 ID가 일치해야 합니다.", "iveliz", member.getMemberId());
    }
	
    @Test
    public void testGetTotalPostCount_withEmptySearchTerm() {
        PostDAO dao = new PostDAO();
        
        String searchTerm = ""; // 빈 검색어
        
        int totalCount = dao.getTotalPostCount(searchTerm);
        
        assertTrue("빈 검색어여도 총 게시글 수는 0개 이상이어야 합니다.", totalCount >= 0);
    }
    @Test
    public void testRequestSetsCategoryList() throws ServletException, IOException {
        // 1. request.setAttribute()와 getAttribute()만 구현
        HttpServletRequest request = new HttpServletRequest() {
            private final Map<String, Object> attributes = new HashMap<>();

            @Override
            public void setAttribute(String name, Object value) {
                attributes.put(name, value);
            }

            @Override
            public Object getAttribute(String name) {
                return attributes.get(name);
            }

            // 나머지 메서드는 생략 가능
        };

        // 2. 액션 실행
        GetCategoryNameAction action = new GetCategoryNameAction();
        String result = action.execute(request);

        // 3. 결과 검증
        assertEquals("header.jsp", result);

        Object categoryList = request.getAttribute("categoryList");
        assertNotNull("categoryList가 null이면 안 됩니다", categoryList);
        assertTrue("categoryList는 List<PostVO> 타입이어야 합니다", categoryList instanceof List);
    }
}
