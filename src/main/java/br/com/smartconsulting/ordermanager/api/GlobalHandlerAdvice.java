package br.com.smartconsulting.ordermanager.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.smartconsulting.ordermanager.core.common.exceptions.InvalidParameterException;
import br.com.smartconsulting.ordermanager.core.common.exceptions.NotFoundException;

@ControllerAdvice
public class GlobalHandlerAdvice {

	@ExceptionHandler(value = { NotFoundException.class })
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorMessage notFound(NotFoundException exception) {
		return new ErrorMessage(exception.getMessage());
	}

	@ExceptionHandler(value = { ConstraintViolationException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorMessage constraint(ConstraintViolationException exception) {
		return new ErrorMessage(exception.getMessage());
	}
	
	@ExceptionHandler(value = { InvalidParameterException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorMessage invalidParameter(InvalidParameterException exception) {
		return new ErrorMessage(exception.getMessage());
	}
	
	@ExceptionHandler(value = { MethodArgumentNotValidException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorMessage methodArgumentNotValid(MethodArgumentNotValidException ex) {
		 List<String> errors = ex.getBindingResult().getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());
		 
		return new ErrorMessage(errors);
	}
}
