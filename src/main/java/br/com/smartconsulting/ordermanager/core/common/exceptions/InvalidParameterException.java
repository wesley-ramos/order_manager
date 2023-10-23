package br.com.smartconsulting.ordermanager.core.common.exceptions;

public class InvalidParameterException extends RuntimeException {

	private static final long serialVersionUID = -2901098338767785539L;

	public InvalidParameterException(String message) {
		super(message);
	}
}
