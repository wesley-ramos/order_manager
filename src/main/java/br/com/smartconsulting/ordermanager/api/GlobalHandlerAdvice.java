package br.com.smartconsulting.ordermanager.api;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
}
