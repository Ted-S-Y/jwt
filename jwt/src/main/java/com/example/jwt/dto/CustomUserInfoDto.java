package com.example.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomUserInfoDto extends MemberDto {
	private Long memberId;
	
	private String email;
	
	private String name;
	
	private String password;
	
	private RoleType role;

}
