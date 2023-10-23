package br.com.smartconsulting.ordermanager.core.common.exceptions;

public class InvalidOperationException extends RuntimeException {

	private static final long serialVersionUID = 3449502661710036077L;

	public InvalidOperationException(String message) {
        super(message);
    }
}
