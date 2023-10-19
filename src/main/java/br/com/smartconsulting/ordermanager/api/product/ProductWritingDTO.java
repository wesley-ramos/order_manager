package br.com.smartconsulting.ordermanager.api.product;

import javax.validation.constraints.NotBlank;

public class ProductWritingDTO {
	
	@NotBlank(message = "The name field is required")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
