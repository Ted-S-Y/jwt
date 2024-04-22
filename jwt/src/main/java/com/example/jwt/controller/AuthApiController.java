package com.example.jwt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwt.dto.LoginRequestDto;
import com.example.jwt.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthApiController {

	private final AuthService authService;
	
	public ResponseEntity<String> getMemberProfile(
			@Valid @RequestBody LoginRequestDto request
			){
		String token = this.authService.login(request);
		
		return ResponseEntity.status(HttpStatus.OK).body(token);
	}
	
}
