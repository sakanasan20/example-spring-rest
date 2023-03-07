package tw.niq.example.spring.rest.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import tw.niq.example.spring.rest.model.response.ErrorResponseModel;

@ControllerAdvice
public class UserExceptionHandler {
	
	@ExceptionHandler(BadRequestUserException.class)
	public ResponseEntity<ErrorResponseModel> handleBadRequestUserException(BadRequestUserException ex, WebRequest webRequest) {
		System.out.println(ex);
		return new ResponseEntity<>(
				new ErrorResponseModel(LocalDateTime.now(), ex.getMessage()), 
				new HttpHeaders(), 
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceNotFoundUserException.class)
	public ResponseEntity<ErrorResponseModel> handleResourceNotFoundUserException(ResourceNotFoundUserException ex, WebRequest webRequest) {
		System.out.println(ex);
		return new ResponseEntity<>(
				new ErrorResponseModel(LocalDateTime.now(), ex.getMessage()), 
				new HttpHeaders(), 
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorResponseModel> handleUserException(UserException ex, WebRequest webRequest) {
		
		return new ResponseEntity<>(
				new ErrorResponseModel(LocalDateTime.now(), ex.getMessage()), 
				new HttpHeaders(), 
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseModel> handleException(Exception ex, WebRequest webRequest) {
		
		return new ResponseEntity<>(
				new ErrorResponseModel(LocalDateTime.now(), ex.getMessage()), 
				new HttpHeaders(), 
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
