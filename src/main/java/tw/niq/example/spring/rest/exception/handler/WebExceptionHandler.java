package tw.niq.example.spring.rest.exception.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import tw.niq.example.spring.rest.exception.BadRequestException;
import tw.niq.example.spring.rest.exception.ResourceNotFoundException;
import tw.niq.example.spring.rest.exception.UserException;
import tw.niq.example.spring.rest.model.ErrorModel;

@ControllerAdvice
public class WebExceptionHandler {
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorModel> handleBadRequestUserException(BadRequestException ex, WebRequest webRequest) {
		System.out.println(ex);
		return new ResponseEntity<>(
				new ErrorModel(LocalDateTime.now(), ex.getMessage()), 
				new HttpHeaders(), 
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorModel> handleResourceNotFoundUserException(ResourceNotFoundException ex, WebRequest webRequest) {
		System.out.println(ex);
		return new ResponseEntity<>(
				new ErrorModel(LocalDateTime.now(), ex.getMessage()), 
				new HttpHeaders(), 
				HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorModel> handleAccessDeniedException(AccessDeniedException ex, WebRequest webRequest) {
		
		return new ResponseEntity<>(
				new ErrorModel(LocalDateTime.now(), ex.getMessage()), 
				new HttpHeaders(), 
				HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorModel> handleUserException(UserException ex, WebRequest webRequest) {
		
		return new ResponseEntity<>(
				new ErrorModel(LocalDateTime.now(), ex.getMessage()), 
				new HttpHeaders(), 
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorModel> handleException(Exception ex, WebRequest webRequest) {
		
		return new ResponseEntity<>(
				new ErrorModel(LocalDateTime.now(), ex.getMessage()), 
				new HttpHeaders(), 
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
