package com.example.jwt.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.jwt.service.CustomUserDetails;
import com.example.jwt.service.CustomUserDetailsService;
import com.example.jwt.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
	
	private final CustomUserDetailsService customUserDetailsService;
	private final JwtUtil jwtUtil;
	
	
	/**
	 * JWT 토큰 검증 필터
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String authorizationHeader = request.getHeader("Authorization");
		
		// JWT가 헤더에 있는 경우
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			
			// JWT 유효성 검증
			if (jwtUtil.validateToken(token)) {
				Long userId = jwtUtil.getUserId(token);
				
				// 유저와 토큰 일치 시 userDetails 생성
				UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId.toString());
				
				if (userDetails != null) {
					// UserDetails, Password, Role -> 접근권한 인증 Token 생성
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					
					// 현재 Request의 Security Context에 접근권한 설정
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
		}
	
		// 다음 필터로 넘기기
		filterChain.doFilter(request, response);
			
	}

}
