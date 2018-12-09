package com.appsdeveloperblog.app.ws.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessage;

@ControllerAdvice
public class AppExceptionHandler {

	@ExceptionHandler(value= {UserServiceException.class})
	//this method should return ResponseEntity(of generic one) 
	public ResponseEntity<Object> handleUserServiceException(UserServiceException ex,WebRequest request){
		
		ErrorMessage errorMessage = new ErrorMessage(new Date(),ex.getMessage());
		return new ResponseEntity<>(errorMessage,new HttpHeaders(),HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(value= {Exception.class})
	//this method should return ResponseEntity(of generic one) 
	public ResponseEntity<Object> handleOtherException(Exception ex,WebRequest request){
		
		ErrorMessage errorMessage = new ErrorMessage(new Date(),ex.getMessage());
		return new ResponseEntity<>(errorMessage,new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
