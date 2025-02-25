package Services;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailService {

    private final String senderEmail;
    private final String senderPassword;
    private final Properties properties;

    public EmailService() {
        // Charger les informations d'identification par email de manière sécurisée
        this.senderEmail = "hopefulhearts.3a12@gmail.com"; // Utiliser des variables d'environnement
        this.senderPassword = "Asmakrimi2002*"; // Utiliser des variables d'environnement

        // Configuration SMTP pour Gmail
        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    }

    /**
     * Envoie un email avec un sujet et un message personnalisés.
     *
     * @param recipient    L'adresse email du destinataire
     * @param subject      Le sujet de l'email
     * @param messageBody  Le contenu de l'email (peut être en texte brut ou HTML)
     * @param isHtml       Définir sur vrai si le corps du message est formaté en HTML, sinon faux
     */
    public void sendEmail(String recipient, String subject, String messageBody, boolean isHtml) {
        if (senderEmail == null || senderPassword == null) {
            System.err.println("Erreur : Les informations d'identification de l'email ne sont pas configurées.");
            return;
        }

        // Créer une session de mail avec authentification
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Créer le message email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);

            if (isHtml) {
                message.setContent(messageBody, "text/html");
            } else {
                message.setText(messageBody);
            }

            // Envoyer l'email
            Transport.send(message);
            System.out.println("✅ Email envoyé avec succès à " + recipient);

        } catch (MessagingException e) {
            System.err.println("❌ Erreur lors de l'envoi de l'email : " + e.getMessage());
            e.printStackTrace();
        }
    }
}