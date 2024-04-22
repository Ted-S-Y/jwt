package com.example.jwt.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jwt.dto.CustomUserInfoDto;
import com.example.jwt.dto.LoginRequestDto;
import com.example.jwt.entity.Member;
import com.example.jwt.repository.MemberRepository;
import com.example.jwt.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
	
	private final JwtUtil jwtUtil;
	private MemberRepository memberRepository;
	private final PasswordEncoder encoder;
	private final ModelMapper modelMapper;
	
	
	@Transactional
	public String login(LoginRequestDto dto) {
		String email = dto.getEmail();
		String password = dto.getPassword();
		Member member = memberRepository.findMemberByEmail(email);
		
		if (member == null) {
			throw new UsernameNotFoundException("Email can't find");
		}
		
		// 암호화된 password를 디코딩한 값과 입력한 패스워드 값이 다르면 null
		if(!encoder.matches(password, member.getPassword())) {
			throw new BadCredentialsException("Password is not match");
		}
		
		CustomUserInfoDto info = modelMapper.map(member, CustomUserInfoDto.class);
		
		String accessToken = jwtUtil.createAccessToken(info);
		
		return accessToken;
	}

}
