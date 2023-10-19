package br.com.smartconsulting.ordermanager.api.product;

public class ProductReadingDTO {
	private Long id;
	private String name;
	
	public ProductReadingDTO(Long id, String name){
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
