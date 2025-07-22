package com.camp.model;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

public class MemberDAO {

	// 占쎌돳占쎌뜚揶쏉옙占쎌뿯
	public boolean addMember(MemberVO vo){
		SqlSession session = DBCP.getSqlSessionFactory().openSession();
		boolean result = false;

		// memberImage揶쏉옙 null占쎌뵠椰꾧퀡援� �⑤벉媛싷옙�뵬 野껋럩�뒭 占쎈탵占쎈쨨占쎈뱜 占쎌뵠沃섎챷占� 占쎄퐬占쎌젟
		if (vo.getMemberImage() == null || vo.getMemberImage().isEmpty()) {
			vo.setMemberImage("defaultMemberImage.png");
		}

		try{
			session.insert("memberMapper.addMember", vo);
			session.commit();
			result = true;
		}catch(Exception e){
			session.rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
		return result;
	}

	// 媛��엯�떆 ID 以묐났 �솗�씤
	public boolean isDuplicateId(String value) {
		SqlSession session = DBCP.getSqlSessionFactory().openSession();
		try {
			int count = session.selectOne("memberMapper.isDuplicateId", value);
			return count > 0;
		} finally {
			session.close();
		}
	}

	// 媛��엯�떆 �씠硫붿씪 以묐났 �솗�씤
	public boolean isDuplicateEmail(String email) {
		SqlSession session = DBCP.getSqlSessionFactory().openSession();
		try {
			int count = session.selectOne("memberMapper.isDuplicateEmail", email);
			return count > 0;
		} finally {
			session.close();
		}
	}
	
	public boolean isDuplicateEmailExcludingSelf(String email, String memberId) {
	    SqlSession session = DBCP.getSqlSessionFactory().openSession();
	    try {
	        Map<String, String> param = new HashMap<>();
	        param.put("email", email);
	        param.put("memberId", memberId);
	        int count = session.selectOne("memberMapper.isDuplicateEmailExcludingSelf", param);
	        return count > 0;
	    } finally {
	        session.close();
	    }
	}


	// 媛��엯�떆 �땳�꽕�엫 以묐났 �솗�씤
	public boolean isDuplicateNickName(String nickName) {
		SqlSession session = DBCP.getSqlSessionFactory().openSession();
		try {
			int count = session.selectOne("memberMapper.isDuplicateNickName", nickName);
			return count > 0;
		} finally {
			session.close();
		}
	}
	
	public boolean isDuplicateNickNameExcludingSelf(String nickName, String memberId) {
	    SqlSession session = DBCP.getSqlSessionFactory().openSession();
	    try {
	        Map<String, String> param = new HashMap<>();
	        param.put("nickname", nickName);
	        param.put("memberId", memberId);
	        int count = session.selectOne("memberMapper.isDuplicateNickNameExcludingSelf", param);
	        return count > 0;
	    } finally {
	        session.close();
	    }
	}


	public List<MemberVO> getMembers(){
		List<MemberVO> list = null;
		SqlSession conn = DBCP.getSqlSessionFactory().openSession();
		list = conn.selectList("memberMapper.getMembers");
		conn.close();
		return list;
	}

	// 占쎄땀 占쎌젟癰귨옙 占쎈땾占쎌젟 占쎌읈 �뜮袁⑨옙甕곕뜇�깈 占쎌넇占쎌뵥
	public boolean pwCheck(MemberVO vo) {
		SqlSession session = DBCP.getSqlSessionFactory().openSession();
		boolean result = false;

		try {
			int count = session.selectOne("memberMapper.pwCheck", vo);
			result = (count == 1);
		} finally {
			session.close();
		}
		return result;
	}

	// 占쎄땀 占쎌젟癰귨옙 占쎈땾占쎌젟
	public boolean updateProfile(MemberVO vo){
		SqlSession session = DBCP.getSqlSessionFactory().openSession();
		boolean result = false;

		try {
			int updated = session.update("memberMapper.updateProfile", vo);
			if (updated == 1){
				session.commit();
				result = true;
			}
		} catch(Exception e){
			session.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	// �뜮袁⑨옙甕곕뜇�깈 占쎈땾占쎌젟
	public boolean updatePw(String memberId, String pw, String newPw){
		SqlSession session = DBCP.getSqlSessionFactory().openSession();
		boolean result = false;
		HashMap<String, String> map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("pw", pw);
		map.put("newPw", newPw);
		try{
			int i = session.update("memberMapper.updatePw", map);
			if (i == 1){
				session.commit();
				result = true;
			}
		}catch(Exception e){
			session.rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
		return result;
	}

	// 占쎈늄嚥≪뮉釉� 占쎄텢筌욑옙 鈺곌퀬�돳
	public String getMemberImage(String memberId) {
		SqlSession session = DBCP.getSqlSessionFactory().openSession();
		String imageName = null;

		try {
			imageName = session.selectOne("memberMapper.getMemberImage", memberId);
		} finally {
			session.close();
		}
		return imageName;
	}

	// 占쎈늄嚥≪뮉釉� 占쎄텢筌욑옙 占쎈쾻嚥∽옙
	public boolean setMemberImage(String memberId, String memberImage){
		SqlSession session = DBCP.getSqlSessionFactory().openSession();
		boolean result = false;

		HashMap<String, String> map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("memberImage", memberImage);

		try{
			int updated = session.update("memberMapper.setMemberImage", map);
			if (updated == 1){
				session.commit();
				result = true;
			}
		}catch(Exception e){
			session.rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
		return result;
	}

	// 占쎈늄嚥≪뮉釉� 占쎄텢筌욑옙 占쎈땾占쎌젟 獄쏉옙 疫꿸퀣�� 占쎌뵠沃섎챷占� 占쎈솁占쎌뵬 占쎄텣占쎌젫
	public boolean updateMemberImage(String memberId, String newImageFileName, String uploadDirPath) {
		SqlSession session = DBCP.getSqlSessionFactory().openSession();
		boolean result = false;

		try {
			// 1. 疫꿸퀣�� 占쎌뵠沃섎챷占� 鈺곌퀬�돳
			String oldImage = session.selectOne("memberMapper.getMemberImage", memberId);

			// 2. DB 癰귨옙野껓옙(占쎄퉱 占쎌뵠沃섎챷占썸에占�)
			HashMap<String, String> map = new HashMap<>();
			map.put("memberId", memberId);
			map.put("memberImage", newImageFileName);

			int updated = session.update("memberMapper.setMemberImage", map);

			// 3. 占쎄쉐�⑤벏釉�筌롳옙 �뚣끇而� + 疫꿸퀣�� 占쎌뵠沃섎챷占� 占쎄텣占쎌젫
			if (updated == 1) {
				session.commit();
				result = true;

				if (oldImage != null && !oldImage.equals("defaultMemberImage.png")) {
					deleteImageFile(oldImage, uploadDirPath);
				}
			}
		} catch (Exception e) {
			session.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return result;
	}


	// 占쎈늄嚥≪뮉釉� 占쎄텢筌욑옙 占쎄텣占쎌젫 + 占쎈탵占쎈쨨占쎈뱜 占쎌뵠沃섎챷占� 占쎄퐬占쎌젟
	public boolean deleteAndSetDefaultImage(MemberVO vo, String uploadDirPath) {
		String memberId = vo.getMemberId();

		// 1. 疫꿸퀣�� 占쎌뵠沃섎챷占� 占쎈솁占쎌뵬筌륅옙 �빊遺욱뀱(VO)
		String oldImage = vo.getMemberImage();

		// 2. DB 占쎈씜占쎈쑓占쎌뵠占쎈뱜: 占쎈탵占쎈쨨占쎈뱜 占쎌뵠沃섎챷占썸에占� 癰귨옙野껓옙
		boolean updated = setDefaultImage(memberId);

		// 3. 疫꿸퀣�� 占쎌뵠沃섎챷占� 占쎈솁占쎌뵬 占쎄텣占쎌젫
		if (updated && oldImage != null && !oldImage.equals("defaultMemberImage.png")) {
			deleteImageFile(oldImage, uploadDirPath);

			// 4. memberImage 占쎈씜占쎈쑓占쎌뵠占쎈뱜(VO)
			vo.setMemberImage("defaultMemberImage.png");
		}

		return updated;
	}

	// 筌롢끇苡�占쎌뵠沃섎챷占� 占쎄텣占쎌젫 占쎄땀�겫占� 筌롫뗄苑뚳옙諭�1: DB占쎈퓠占쎄퐣 memberImage�몴占� 占쎈탵占쎈쨨占쎈뱜 占쎌뵠沃섎챷占썸에占� 占쎄퐬占쎌젟
	private boolean setDefaultImage(String memberId) {
		SqlSession session = DBCP.getSqlSessionFactory().openSession();
		boolean result = false;

		try {
			int updated = session.update("memberMapper.setDefaultImage", memberId);
			if (updated == 1) {
				session.commit();
				result = true;
			}
		} catch (Exception e) {
			session.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return result;
	}

	// 筌롢끇苡�占쎌뵠沃섎챷占� 占쎄텣占쎌젫 占쎄땀�겫占� 筌롫뗄苑뚳옙諭�2: 占쎄퐣甕곌쑴肉� 占쏙옙占쎌삢占쎈쭆 疫꿸퀣�� 占쎌뵠沃섎챷占� 占쎈솁占쎌뵬 占쎄텣占쎌젫
	private void deleteImageFile(String imageFileName, String uploadDirPath) {
		if (imageFileName == null || imageFileName.isEmpty()) return;

		Path path = Paths.get(uploadDirPath, imageFileName);
		File file = path.toFile();

		if (file.exists()) {
			file.delete();
		}
	}

	// 筌띾뜆�뵠占쎈읂占쎌뵠筌욑옙 鈺곌퀬�돳(占쎄땀 占쎌젟癰귨옙)
	public MemberVO getMyInfo(String memberId) {
	    SqlSession session = DBCP.getSqlSessionFactory().openSession();
	    try {
	        MemberVO vo = session.selectOne("memberMapper.getMyInfo", memberId);
	        return vo;
	    } finally {
	        session.close();
	    }
	}

	// 占쎄땀揶쏉옙 占쎈쿀 疫뀐옙 �뵳�딅뮞占쎈뱜 鈺곌퀬�돳
	public List<HashMap<String, Object>> getMyPosts(String memberId) {
		SqlSession session = DBCP.getSqlSessionFactory().openSession();
		try {
			return session.selectList("memberMapper.getMyPosts", memberId);
		} finally {
			session.close();
		}
	}

	// �겫怨룹춳占쎄쾿占쎈립 疫뀐옙 �뵳�딅뮞占쎈뱜 鈺곌퀬�돳
	public List<HashMap<String, Object>> getMyBookmarks(String memberId) {
		SqlSession session = DBCP.getSqlSessionFactory().openSession();
		try {
			return session.selectList("memberMapper.getMyBookmarks", memberId);
		} finally {
			session.close();
		}
	}

	// 占쎄맒占쏙옙獄쏉옙 筌띾뜆�뵠 占쎈읂占쎌뵠筌욑옙 鈺곌퀬�돳
	public MemberVO getYourInfo(String memberId) {
		SqlSession session = DBCP.getSqlSessionFactory().openSession();
		try {
			return session.selectOne("memberMapper.getYourInfo", memberId);
		} finally {
			session.close();
		}
	}

	
	public boolean updateProfileImage(String memberId, String fileName) {
	    SqlSession session = DBCP.getSqlSessionFactory().openSession();
	    boolean result = false;

	    try {
	        Map<String, Object> param = new HashMap<>();
	        param.put("memberId", memberId);
	        param.put("fileName", fileName);

	        session.update("memberMapper.updateProfileImage", param);
	        session.commit();
	        result = true;
	    } catch (Exception e) {
	        session.rollback();
	        e.printStackTrace();
	    } finally {
	        session.close();
	    }

	    return result;
	}
	
	public boolean updateMemberInfo(MemberVO vo) {
	    SqlSession session = DBCP.getSqlSessionFactory().openSession();
	    boolean result = false;

	    try {
	        int count = session.update("memberMapper.updateMemberInfo", vo);
	        session.commit();
	        result = (count > 0); // 업데이트 성공 여부
	    } catch (Exception e) {
	        session.rollback();
	        e.printStackTrace();
	    } finally {
	        session.close();
	    }

	    return result;
	}
	
	public boolean deleteMember(String memberId, String pw) {
	    SqlSession session = DBCP.getSqlSessionFactory().openSession();
	    boolean result = false;

	    try {
	        // 디버깅 로그 1: 전달받은 입력 값 확인
	        System.out.println("[deleteMember] 입력 memberId: [" + memberId + "]");
	        System.out.println(" [deleteMember] 입력 pw: [" + pw + "]");

	        Map<String, String> param = new HashMap<>();
	        param.put("memberId", memberId);
	        param.put("pw", pw.trim());

	        // 디버깅 로그 2: Mapper 실행 직전 로그
	        System.out.println(" [deleteMember] MyBatis param: " + param);

	        int deleted = session.delete("memberMapper.deleteMember", param);
	        session.commit();

	        // 디버깅 로그 3: 결과 확인
	        System.out.println("[deleteMember] 삭제된 행 수: " + deleted);

	        result = (deleted == 1);
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.rollback();
	    } finally {
	        session.close();
	    }

	    return result;
	}
	
	public String getPasswordById(String memberId) {
	    SqlSession session = DBCP.getSqlSessionFactory().openSession();
	    String pw = null;
	    try {
	        pw = session.selectOne("memberMapper.getPasswordById", memberId);
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        session.close();
	    }
	    return pw;
	}
	
	public boolean updatePassword(String memberId, String newPw) {
	    SqlSession session = DBCP.getSqlSessionFactory().openSession();
	    boolean result = false;

	    try {
	        Map<String, Object> param = new HashMap<>();
	        param.put("memberId", memberId);
	        param.put("pw", newPw);

	        int rows = session.update("memberMapper.updatePassword", param);
	        session.commit();
	        result = rows > 0;
	    } catch (Exception e) {
	        session.rollback();
	        e.printStackTrace();
	    } finally {
	        session.close();
	    }

	    return result;
	}




	
	







}