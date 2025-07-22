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

		PostVO post = service.getPostDetails(1); // postId=1�� �Խñ�

		assertNotNull("�Խñ��� �����ؾ� �մϴ�.", post);
		assertEquals("�Խñ� ID�� ��ġ�ؾ� �մϴ�.", 1, post.getPostId());
	}
	
	@Test
    public void testGetMyInfo() {
        MemberDAO dao = new MemberDAO();
        
        MemberVO member = dao.getMyInfo("iveliz"); // memberId=1�� ȸ��
        
        assertNotNull("ȸ�� ������ null�̸� �� �˴ϴ�.", member);
        assertEquals("ȸ�� ID�� ��ġ�ؾ� �մϴ�.", "iveliz", member.getMemberId());
    }
	
    @Test
    public void testGetTotalPostCount_withEmptySearchTerm() {
        PostDAO dao = new PostDAO();
        
        String searchTerm = ""; // �� �˻���
        
        int totalCount = dao.getTotalPostCount(searchTerm);
        
        assertTrue("�� �˻���� �� �Խñ� ���� 0�� �̻��̾�� �մϴ�.", totalCount >= 0);
    }
    @Test
    public void testRequestSetsCategoryList() throws ServletException, IOException {
        // 1. request.setAttribute()�� getAttribute()�� ����
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

            // ������ �޼���� ���� ����
        };

        // 2. �׼� ����
        GetCategoryNameAction action = new GetCategoryNameAction();
        String result = action.execute(request);

        // 3. ��� ����
        assertEquals("header.jsp", result);

        Object categoryList = request.getAttribute("categoryList");
        assertNotNull("categoryList�� null�̸� �� �˴ϴ�", categoryList);
        assertTrue("categoryList�� List<PostVO> Ÿ���̾�� �մϴ�", categoryList instanceof List);
    }
}
