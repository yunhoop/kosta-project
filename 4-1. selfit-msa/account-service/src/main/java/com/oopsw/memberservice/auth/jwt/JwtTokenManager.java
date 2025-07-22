package com.oopsw.memberservice.auth.jwt;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenManager {

	private static SecretKey getSigningKey() {
		byte[] keyBytes = Base64.getEncoder().encode(JwtProperties.SECRET.getBytes());
		return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
	}

	public static String createJwtToken(String memberId) {
		return Jwts.builder()
			.setSubject("member-token")
			.setExpiration(new Date(System.currentTimeMillis() + JwtProperties.TIMEOUT))
			.setIssuedAt(new Date())
			.claim("memberId", memberId)
			.signWith(getSigningKey(), SignatureAlgorithm.HS512)
			.compact();
	}

	public static boolean validateJwtToken(String token) {
		try {
			Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public static Claims decodeJwt(String token) {
		return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}
}
