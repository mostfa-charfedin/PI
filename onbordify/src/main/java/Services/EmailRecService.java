package Services;

import javafx.scene.control.Alert;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailRecService {

    private static final String MY_EMAIL = "testprojetpi@gmail.com";
    private static final String PASSWORD = "dadl rdqf snwj pzmq";

    public void sendEmail(String recipientEmail, String emailMessage, String subject) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MY_EMAIL, PASSWORD);
            }
        });

        try {
            Message message = prepareMessage(session, recipientEmail, emailMessage, subject);
            if (message != null) {
                Transport.send(message);
                System.out.println("✅ Email envoyé à " + recipientEmail);
            } else {
                System.out.println("❌ Échec de la création du message.");
            }

        } catch (MessagingException e) {
            System.err.println("❌ Erreur d'envoi d'email : " + e.getMessage());
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Message prepareMessage(Session session, String recipientEmail, String msg, String subject) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MY_EMAIL));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject(subject);
            message.setText(msg);
            return message;
        } catch (MessagingException e) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
}
