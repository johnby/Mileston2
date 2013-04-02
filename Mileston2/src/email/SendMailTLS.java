package email;

 
import java.util.Properties;
 
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
public class SendMailTLS {
 
	public static void SendEmail(String email, String subject, String message)
	{
		final String username = "pollserverv2020@gmail.com";
		final String password = "pollsrv2020";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			Message message1 = new MimeMessage(session);
			message1.setFrom(new InternetAddress("from-email@gmail.com"));
			message1.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(email));
			message1.setSubject(subject);
			message1.setText(message);
 
			Transport.send(message1);
 
			System.out.println("Mail sent.");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}


}