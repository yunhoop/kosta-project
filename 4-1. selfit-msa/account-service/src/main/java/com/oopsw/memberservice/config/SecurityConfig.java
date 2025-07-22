package com.oopsw.memberservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.oopsw.memberservice.auth.AuthenticatedUser;
import com.oopsw.memberservice.auth.jwt.JwtAuthenticationFilter;
import com.oopsw.memberservice.auth.jwt.JwtProperties;
import com.oopsw.memberservice.auth.jwt.JwtTokenManager;
import com.oopsw.memberservice.auth.service.CustomOAuth2UserService;
import com.oopsw.memberservice.auth.service.CustomUserDetailsService;
import com.oopsw.memberservice.jpa.MemberRepository;
import com.oopsw.memberservice.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws
		Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService,
		AuthenticationManager authenticationManager, MemberRepository memberRepository,
		CustomUserDetailsService customUserDetailsService, RefreshTokenService refreshTokenService) throws Exception {
		http.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.formLogin(form -> form.disable())
			.httpBasic(httpBasic -> httpBasic.disable());

		http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

		http.addFilter(
			new JwtAuthenticationFilter(authenticationManager, customOAuth2UserService, refreshTokenService));

		http.oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
			.successHandler(oAuth2SuccessHandler())
			.failureHandler(oAuth2FailureHandler()));
		return http.build();

	}

	@Bean
	public AuthenticationSuccessHandler oAuth2SuccessHandler() {
		return (request, response, authentication) -> {
			AuthenticatedUser authenticatedUser = (AuthenticatedUser)authentication.getPrincipal();
			String jwtToken = JwtTokenManager.createJwtToken(authenticatedUser.getMemberId());
			response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

			String html = """
				<!DOCTYPE html>
				<html lang="ko">
				<head><meta charset="UTF-8"><title>로그인 처리중</title></head>
				<body>
				<script>
				  const token = "%s%s";
				  if (window.opener) {
				    window.opener.postMessage({ token: token }, "http://127.0.0.1:8880");
				  }
				  setTimeout(() => window.close(), 100);
				</script>
				<p>로그인 처리 중입니다...</p>
				</body>
				</html>
				""".formatted(JwtProperties.TOKEN_PREFIX, jwtToken);

			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(html);

		};
	}

	@Bean
	public AuthenticationFailureHandler oAuth2FailureHandler() {
		return (request, response, exception) -> {
			String email = (String)request.getAttribute("email");
			String name = (String)request.getAttribute("name");
			String redirectUrl = "http://127.0.0.1:8880/html/account/signup-oauth.html";

			String html = """
				<!DOCTYPE html>
				<html lang="ko">
				<head><meta charset="UTF-8"><title>회원가입</title></head>
				<body>
				<script>
				    if (window.opener) {
				        window.opener.postMessage({ redirect: "%s", email: "%s", name: "%s" }, "http://127.0.0.1:8880");
				    }
				    setTimeout(() => window.close(), 100);
				</script>
				<p> 처리 중입니다...</p>
				</body>
				</html>
				""".formatted(redirectUrl, email, name);

			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(html);
		};
	}

}