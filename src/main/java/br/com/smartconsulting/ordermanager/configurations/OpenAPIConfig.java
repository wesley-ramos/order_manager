package br.com.smartconsulting.ordermanager.configurations;

import javax.servlet.ServletContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {
	
	@Bean
	public OpenAPI api(ServletContext servletContext) {
		Info info = new Info()
			.title("Order Manager")
			.description("This project is a simplified version of an order manager")
			.version("1.0.0")
			.license(new License().name("Apache 2.0").url("http://springdoc.org"));
        
		return new OpenAPI()
			.components(new Components())
			.info(info);
	}
}
