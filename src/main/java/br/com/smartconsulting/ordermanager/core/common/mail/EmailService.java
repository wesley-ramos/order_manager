package br.com.smartconsulting.ordermanager.core.common.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	private Logger logger = LoggerFactory.getLogger(EmailService.class);
	private EmailConfig config;
	
	@Autowired
	private EmailService(EmailConfig config) {
		this.config = config;
	}
	 
	public void send(String subject, String content, String to) {
		if(!config.isConfigured()) {
			logger.warn("Unable to send email, service not configured");
			return;
		}
		
		Properties prop = new Properties();
		prop.put("mail.smtp.host", config.getHost());
        prop.put("mail.smtp.port", config.getPort());
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        
        Session session = Session.getInstance(prop,
        new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getEmail(), config.getPassword());
            }
        });
        
        try {
        	Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(config.getEmail()));
			message.setRecipients(
	            Message.RecipientType.TO,
	            InternetAddress.parse(to)
	        );
	        message.setSubject(subject);
	        message.setContent(content, "text/html; charset=utf-8");
	        Transport.send(message);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
}
