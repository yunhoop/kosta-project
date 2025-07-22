package com.oopsw.apigatewayservice.filter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
	Environment environment;

	public AuthorizationHeaderFilter(Environment environment) {
		super(Config.class);
		this.environment = environment;
	}

	//login ->token ->user(with token) -> header(include token)
	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			String headerString = JwtProperties.HEADER_STRING;

			if (!request.getHeaders().containsKey(headerString)) {
				return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
			}

			String authorizationHeader = request.getHeaders().get(headerString).get(0);
			String jwt = authorizationHeader.replace("Bearer ", "");
			String memberId = getMemberIdFromJwt(jwt);
			if (memberId == null) {
				return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
			}

			// 체인 필터 실행 + 후처리 응답 시 헤더 추가
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				ServerHttpResponse response = exchange.getResponse();
				response.getHeaders().add("memberId", memberId);
			}));
		};
	}

	//spring 5.0
	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		log.error(err);

		byte[] bytes = "The requested token is invalid.".getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
		return response.writeWith(Flux.just(buffer));
	}

	private boolean isJwtValid(String jwt) {
		byte[] secretKeyBytes = Base64.getEncoder().encode(environment.getProperty("token.secret").getBytes());
		SecretKey signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
		boolean returnValue = true;
		String subject = null;
		try {
			JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(signingKey).build();
			subject = jwtParser.parseClaimsJws(jwt).getBody().getSubject();
		} catch (Exception ex) {
			returnValue = false;
		}
		if (subject == null || subject.isEmpty()) {
			returnValue = false;
		}
		return returnValue;
	}

	private String getMemberIdFromJwt(String jwt) {
		try {
			byte[] secretKeyBytes = Base64.getEncoder().encode(environment.getProperty("token.secret").getBytes());
			SecretKey signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

			JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(signingKey).build();

			return jwtParser.parseClaimsJws(jwt).getBody().get("memberId", String.class);
		} catch (Exception ex) {
			log.error("JWT 파싱 오류: {}", ex.getMessage());
			return null;
		}
	}

	public static class Config {
		// Put configuration properties here
	}

}