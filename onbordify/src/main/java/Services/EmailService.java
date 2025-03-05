package Services;

import javafx.scene.control.Alert;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import Models.User;

public class EmailService {

    private static final String MY_EMAIL = "testprojetpi@gmail.com";
    private static final String PASSWORD = "dadl rdqf snwj pzmq";

    public void sendEmail(String recipientEmail, String emailMessage, String subject) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Create session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MY_EMAIL, PASSWORD);
            }
        });

        try {
            Message message = prepareMessage(session, MY_EMAIL, recipientEmail, emailMessage , subject);

            if (message != null) {
                Transport.send(message);
                new Alert(Alert.AlertType.INFORMATION, "Email Sent Successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to create email. Please try again.").show();
            }

        } catch (MessagingException e) {
            new Alert(Alert.AlertType.ERROR, "Error Sending Email: " + e.getMessage()).show();
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Message prepareMessage(Session session, String myEmail, String recipientEmail, String msg, String subject) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
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
