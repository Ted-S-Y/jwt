package com.example.jwt.handler;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.jwt.Exception.AlreadyExistElementException;
import com.example.jwt.dto.ErrorResponseDto;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice // 전역 컨트롤러 예외 핸들러
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	public static final String TRACE = "trace";
	
	@Value("${error.printStackTrace}")
	private boolean printStackTrace;
	
	/**
	 * Spring 내부에서 예외 발생시 호출
	 * buildErrorResponse를 통해 예외 처리
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatusCode statusCode, WebRequest request) {
		// TODO Auto-generated method stub
		return buildErrorResponse(ex, ex.getMessage(), HttpStatus.valueOf(statusCode.value()), request);
	}
	
	/**
	 * 커스텀예외 DTO 형태로 응답 생성
	 * @param exception
	 * @param message
	 * @param httpStatus
	 * @param request
	 * @return
	 */
	private ResponseEntity<Object> buildErrorResponse(Exception exception,
			String message,
			HttpStatus httpStatus,
			WebRequest request) {
		ErrorResponseDto dto = new ErrorResponseDto(httpStatus.value(), message, LocalDateTime.now());
		
		if (printStackTrace && isTraceOn(request)) {
			dto.setStackTrace(ExceptionUtils.getStackTrace(exception));
		}
		
		return ResponseEntity.status(httpStatus).body(dto);
	}
	
	
	/**
	 * trace on 여부
	 * @param request
	 * @return
	 */
	private boolean isTraceOn(WebRequest request) {
		String[] value = request.getParameterValues(TRACE);
		return Objects.nonNull(value)
				&& value.length > 0
				&& value[0].contentEquals("true");
	}
	
	/**
	 * 412 Validate Exception
	 * MethodArgumentNotValidException 특화 처리
	 * 주로 DTO에 대한 유효성 검사 실패시 발생
	 * - @Valid 어노테이션 사용하여 유효성 검사를 수행한 경우 발생
	 */
	@Override
	@Hidden
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		// TODO Auto-generated method stub
		ErrorResponseDto dto = new ErrorResponseDto(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Validation error. Check 'errors' field for details", LocalDateTime.now());
		
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			dto.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
		}

		return ResponseEntity.unprocessableEntity().body(dto);
	}
	
	/**
	 * 403 Access Denied Exception
	 * 
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler(AccessDeniedException.class)
	@Hidden
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception, WebRequest request) {
		log.error("Access denied", exception);
		return buildErrorResponse(exception, exception.getMessage(), HttpStatus.FORBIDDEN, request);
	}
	
	/**
	 * 409 AlreadyExistElementException
	 * 
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler(AlreadyExistElementException.class)
	@Hidden
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<Object> handleAccessDeniedException(AlreadyExistElementException exception, WebRequest request) {
		log.error("Failed to element is already exist", exception);
		return buildErrorResponse(exception, exception.getMessage(), HttpStatus.FORBIDDEN, request);
	}
	
	/**
	 * 필요시 Exception Handler 추가 - 예상하는 오류는 전부 ExceptionHandler로 처리
	 */
	
	/**
	 * 500 Uncaught Exception
	 * 모든 예외에 대한 Handler
	 *  
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@Hidden
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest request) {
		log.error("Internal error occurred", exception);
		
		return buildErrorResponse(exception, exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
	
}
