package com.example.jwt.Exception;

/**
 * 존재여부Exception
 */
public class AlreadyExistElementException extends RuntimeException {
	public AlreadyExistElementException(String message) {
		super(message);
	}
}
