package com.example.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.jwt.filter.JwtAuthFilter;
import com.example.jwt.handler.CustomAccessDeniedHandler;
import com.example.jwt.handler.CustomAuthenticationEnrtyPoint;
import com.example.jwt.service.CustomUserDetailsService;
import com.example.jwt.util.JwtUtil;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity	// Spring Security 컨택스트 설정 명시
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)	// annotation으로 contoller api 보안수준 설정 활성화
@AllArgsConstructor
public class SecurityConfig {
	private final CustomUserDetailsService customUserDetailsService;
	private final JwtUtil jwtUtil;
	private final CustomAccessDeniedHandler accessDeniedHandler;
	private final CustomAuthenticationEnrtyPoint authenticationEnrtyPoint;
	
	private static final String[] AUTH_WHITELIST = {
			"/api/v1/member/**", "/swagger-ui/**", "api-docs", "/swagger-ui-custom.html",
			"/v3/api-docs/**", "api-docs/**", "/swagger-ui.html", "/api/vi/auth/**"
	};
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// CSRF보호 비활성화, CORS 설정 적용
		http.csrf((csrf) -> csrf.disable());
		http.cors(Customizer.withDefaults());
		
		// 세션 관리 상태가 없음으로 구성. Security가 세션 생성 or 사용 x
		http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		// FormLogin, BasicHTTP 비활성화
		http.formLogin((form)->form.disable());
		http.httpBasic(AbstractHttpConfigurer::disable);
		
		// JwtAuthFiler를 UsernamePasswordAuthenticationFilter 앞에 추가
		http.addFilterBefore(new JwtAuthFilter(customUserDetailsService, jwtUtil), UsernamePasswordAuthenticationFilter.class);
		
		http.exceptionHandling((exceptHandling) -> exceptHandling.authenticationEntryPoint(authenticationEnrtyPoint).accessDeniedHandler(accessDeniedHandler));
		
		// 권한 규칙 작성
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(AUTH_WHITELIST).permitAll()
				// @PreAuthrization을 사용할 것이기 때문에 모든 경로에 대한 인증처리는 Pass
				.anyRequest().permitAll()
				// ..anyRequest().authenticated()
				);
		
		return http.build();
	}
	
}
