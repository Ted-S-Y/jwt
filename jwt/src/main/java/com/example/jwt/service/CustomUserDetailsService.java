package com.example.jwt.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jwt.dto.CustomUserInfoDto;
import com.example.jwt.entity.Member;
import com.example.jwt.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	
	private final MemberRepository memberRepository;
	private final ModelMapper mapper;
	
	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Member member = memberRepository.findById(Long.parseLong(id)).orElseThrow(() -> new UsernameNotFoundException("There is no user"));
		
		CustomUserInfoDto dto = mapper.map(member, CustomUserInfoDto.class);
		
		return new CustomUserDetails(dto);
	}

}
