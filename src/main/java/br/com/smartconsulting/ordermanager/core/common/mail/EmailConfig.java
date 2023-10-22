package br.com.smartconsulting.ordermanager.core.common.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailConfig {
	
	private boolean configured;
	private String host;
	private String port;
	private String email;
	private String password;
	
	public EmailConfig (
			@Value("${mail.smtp.enabled}") boolean configured,
			@Value("${mail.smtp.host:#{null}}") String host,
			@Value("${mail.smtp.port:#{null}}") String port,
			@Value("${mail.smtp.address:#{null}}") String email,
			@Value("${mail.smtp.password:#{null}}") String password) {
		this.configured = configured;
		this.host = host;
		this.port = port;
		this.email = email;
		this.password = password;
	}

	public boolean isConfigured() {
		return configured;
	}

	public String getHost() {
		return host;
	}

	public String getPort() {
		return port;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}
