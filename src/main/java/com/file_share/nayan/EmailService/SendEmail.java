package com.file_share.nayan.EmailService;

import java.io.File;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.file_share.nayan.pojo.UploadFileResponse;

public class SendEmail {
	
	static String message;
	
	static String subject ="shareKaro file sharing";
	
	private static String shareLink;
	private static long fileSize;
	
	@Value("${EMAIL_ID}")
	 static String email_id;
	@Value("${PASSWORD}")
	 static String password;
	
	
	public static void setShareLink(String shareLink) {
		SendEmail.shareLink = shareLink;
	}


	public static void setFileSize(long fileSize) {
		SendEmail.fileSize = Math.round(fileSize/1024);
	}

	// responsible for send text message only
	public static boolean sendMail(String to,String from) {
		
		
		
		message=EmailTemplate.emailContent(shareLink,fileSize, from);
		
	
		String host ="smtp.gmail.com";
		
		Properties properties = System.getProperties();
		// set host
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		
		// step 1
		
		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email_id,password);
			}
			
		});
		
		session.setDebug(true);
		
		
		// step 2 compose the message;
		
		MimeMessage mimeMessage = new MimeMessage(session);
		
		
		try {
			
			mimeMessage.setFrom(email_id);
			
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email_id));
			
			mimeMessage.setSubject(subject);
			
			mimeMessage.setContent(message,"text/html");
			
			// step 3
			Transport.send(mimeMessage);
			
			
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	
		
	

	
		
	
		
	}

	
	
}
