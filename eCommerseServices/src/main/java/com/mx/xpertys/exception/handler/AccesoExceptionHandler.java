package com.mx.xpertys.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.mx.xpertys.dto.ErrorObjeto;
import com.mx.xpertys.exception.AccesoException;

@ControllerAdvice
public class AccesoExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(value= {AccesoException.class})
	public ResponseEntity<Object> handleException(RuntimeException ex, WebRequest request){
		String mensajeException = "Api Rest Error: " + ex.getMessage();
		ErrorObjeto error = new ErrorObjeto();
		error.setError(mensajeException);
		HttpHeaders header = new HttpHeaders();
		return handleExceptionInternal(ex, error, header, HttpStatus.CONFLICT, request);
	}
}
