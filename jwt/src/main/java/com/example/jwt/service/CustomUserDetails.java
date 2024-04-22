package com.example.jwt.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.jwt.dto.CustomUserInfoDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
	
	private final CustomUserInfoDto member;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		List<String> roles = new ArrayList<>();
		roles.add("ROLE_" + member.getRole().toString());
		
		return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}
	
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return  member.getPassword();
	}
	
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return member.getMemberId().toString();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	

}
