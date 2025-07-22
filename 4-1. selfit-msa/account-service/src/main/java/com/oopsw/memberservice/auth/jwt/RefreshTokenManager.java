package com.oopsw.memberservice.auth.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class RefreshTokenManager {

	private static SecretKey getSigningKey() {
		byte[] keyBytes = Base64.getEncoder().encode(JwtProperties.SECRET.getBytes());
		return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
	}

	public static String createRefreshToken() {
		return Jwts.builder()
			.setSubject(UUID.randomUUID().toString()) // 사용자 ID 대신 UUID 사용
			.setId(UUID.randomUUID().toString())      // jti: JWT ID (고유 식별자)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + JwtProperties.TIMEOUT))
			.signWith(getSigningKey(), SignatureAlgorithm.HS512)
			.compact();
	}

	public static void validateRefreshToken(String token) {
		Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
	}
}