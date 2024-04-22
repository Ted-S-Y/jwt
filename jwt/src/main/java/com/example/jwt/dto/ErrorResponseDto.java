package com.example.jwt.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {

	private final int status;
	private final String message;
	private final LocalDateTime time;
	private String stackTrace;	// 백엔드의 정보를 노출시키는 민감정보, 개발단계에서만 반환처리
	private List<ValidationError> validErrors;	// ValidationException 발생시, Request데이터의 validation에서 실패한 리스트
	
	@Data
	@RequiredArgsConstructor
	private static class ValidationError {
		private final String field;
		private final String message;
	}
	
	public void addValidationError(String field, String message) {
		if (Objects.isNull(validErrors)) {
			validErrors = new ArrayList<>();
		}
		
		validErrors.add(new ValidationError(field, message));
	}
}
