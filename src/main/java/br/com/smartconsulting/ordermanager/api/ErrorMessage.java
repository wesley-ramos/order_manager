package br.com.smartconsulting.ordermanager.api;

import java.util.ArrayList;
import java.util.List;

public class ErrorMessage {
	private List<String> errors = new ArrayList<>();

	public ErrorMessage(String message) {
		this.errors.add(message);
	}
	
	public ErrorMessage(List<String> messages) {
		this.errors = messages;
	}

	public List<String> getErrors() {
		return errors;
	}
}
