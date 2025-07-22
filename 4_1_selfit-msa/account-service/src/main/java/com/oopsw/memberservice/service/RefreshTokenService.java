package com.oopsw.memberservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oopsw.memberservice.auth.jwt.AuthResult;
import com.oopsw.memberservice.auth.jwt.JwtTokenManager;
import com.oopsw.memberservice.auth.jwt.RefreshTokenManager;
import com.oopsw.memberservice.jpa.RefreshTokenEntity;
import com.oopsw.memberservice.jpa.RefreshTokenRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	public void rotateTokensAndCreateNew(AuthResult result) {

		//1. refreshToken이 있는지 확인
		String refreshToken = result.getRefreshToken();
		if (refreshToken == null || refreshToken.trim().isEmpty()) {
			throw new RuntimeException("refreshToken is invalid");
		}

		//2. refreshToken이 유효한 jwt인지, 유효기간이 넘었는지 확인
		RefreshTokenManager.validateRefreshToken(result.getRefreshToken());

		//3. refreshToken이 db에서 있는지 조회(jti로 조회)
		Claims claims = JwtTokenManager.decodeJwt(refreshToken);
		String jti = claims.get("jti", String.class);
		RefreshTokenEntity oldRefreshToken = findByJti(jti);

		if (oldRefreshToken.getUsed() == 1) {
			throw new RuntimeException("refreshToken not found");
		}

		System.out.println(oldRefreshToken.getMemberId());
		if (oldRefreshToken.getMemberId().equals(result.getMember().getMemberId())) {
			throw new RuntimeException("invalid refreshToken");
		}

		//4. 새로운 AccessToken 발급 및 refreshToken도 새로 발급
		String newJwtToken = JwtTokenManager.createJwtToken(result.getMember().getMemberId());
		String newRefreshToken = RefreshTokenManager.createRefreshToken();

		//5. 기존에 있는 oldRefreshToken 무효화
		rotateRefreshToken(oldRefreshToken, newRefreshToken, oldRefreshToken.getMemberId());
		result.setAccessToken(newJwtToken);
		result.setRefreshToken(newRefreshToken);
	}

	public void saveRefreshToken(String refreshToken, String memberId) {
		saveRefreshToken(createRefreshToken(refreshToken, memberId));
	}

	public void saveRefreshToken(RefreshTokenEntity refreshToken) {
		refreshTokenRepository.save(refreshToken);
	}

	public RefreshTokenEntity findByJti(String jti) {
		return refreshTokenRepository.findByJti(jti);
	}

	public void rotateRefreshToken(RefreshTokenEntity oldToken, String newRefreshToken, String memberId) {
		oldToken.setUsed(1);
		refreshTokenRepository.save(createRefreshToken(newRefreshToken, memberId));
	}

	public RefreshTokenEntity createRefreshToken(String refreshToken, String memberId) {
		return RefreshTokenEntity.builder()
			.jti(JwtTokenManager.decodeJwt(refreshToken).get("jti", String.class))
			.memberId(memberId)
			.used(0)
			.expiresAt(JwtTokenManager.decodeJwt(refreshToken).getExpiration())
			.build();
	}

}
