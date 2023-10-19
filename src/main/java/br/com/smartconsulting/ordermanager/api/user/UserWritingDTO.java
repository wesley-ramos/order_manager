package br.com.smartconsulting.ordermanager.api.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserWritingDTO {
	
	@NotBlank(message = "The name field is required")
	private String name;
	
	@Email(message = "The e-mail is invalid")
	@NotBlank(message = "The email field is required")
	private String email;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
