package tn.esprit.pi.epione.util;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class Mail {
	@Resource(name = "java:jboss/mail/Gmail")
    private Session session;
 
    public void send(String addresses, String topic,String htmlContent, String textMessage,String path) {
 
        try {
        	Properties props = new Properties();
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            
             session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                      protected PasswordAuthentication getPasswordAuthentication() {
                          return new PasswordAuthentication("pi.dev.esrpit2017@gmail.com","aZERTY123");
                      }
                    });
             System.out.println("111111");
            Message message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(addresses));
            message.setSubject(topic);
            //message.setText(textMessage);
            Multipart multipart = new MimeMultipart("alternative");
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(textMessage);
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlContent, "text/html");
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(htmlPart);
            message.setContent(multipart);
            
            
            message.setHeader("X-Mailer", "MSF");
            Date timeStamp = new Date();
            message.setSentDate(timeStamp);
            Transport.send(message);
 
        } catch (MessagingException e) {
            Logger.getLogger(Mail.class.getName()).log(Level.WARNING, "Cannot send mail", e);
        }
    }
}
