package Services;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailTesting {

    private final String senderEmail;
    private final String senderPassword;
    private final Properties properties;

    public EmailTesting() {
        // Load email credentials securely (avoid hardcoding)
        this.senderEmail = ("asmaakrimiii@gmail.com"); // Set this in environment variables
        this.senderPassword = ("Asmakrimi2002*"); // Set this in environment variables

        // SMTP configuration for Gmail (can be customized for other providers)
        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

    }

    /**
     * Sends an email with a custom subject and message.
     *
     * @param recipient The recipient's email address
     * @param subject   The subject of the email
     * @param messageBody The content of the email (can be plain text or HTML)
     * @param isHtml    Set to true if the message body is HTML formatted, otherwise false
     */
    public void sendEmail(String recipient, String subject, String messageBody, boolean isHtml) {
        if (senderEmail == null || senderPassword == null) {
            System.err.println("Error: Email credentials are not set. Please configure environment variables.");
            return;
        }

        // Create a mail session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);

            if (isHtml) {
                message.setContent(messageBody, "text/html");
            } else {
                message.setText(messageBody);
            }

            // Send email
            Transport.send(message);
            System.out.println("✅ Email sent successfully to " + recipient);

        } catch (MessagingException e) {
            System.err.println("❌ Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EmailService emailService = new EmailService();

        // Test email sending (Replace with actual values)
        String recipient = "akrimi041@gmail.com"; // Replace with the actual recipient email
        String subject = "Test Email";
        String messageBody = "<h3>Hello!</h3><p>This is a test email from the Java email service.</p>";
        boolean isHtml = true; // Set to false if you want plain text

        emailService.sendEmail(recipient, subject, messageBody, isHtml);
    }
}