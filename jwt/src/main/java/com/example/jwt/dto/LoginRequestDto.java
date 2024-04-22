package com.example.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "AUTH_REQ_01 : 로그인 요청 DTO")
public class LoginRequestDto {
	
	@NotNull(message = "이메일 입력은 필수")
	private String email;
	
	@NotNull(message = "패스워드 입력 필수")
	private String password;

}
