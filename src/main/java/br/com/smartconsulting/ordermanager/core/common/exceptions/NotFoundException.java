package br.com.smartconsulting.ordermanager.core.common.exceptions;

public class NotFoundException extends RuntimeException{

	private static final long serialVersionUID = -2795492826146311290L;
	
	public NotFoundException(String message) {
        super(message);
    }
}
