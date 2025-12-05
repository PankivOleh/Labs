
package Utils;

import java.util.Properties;
import javax.mail.*;
        import javax.mail.internet.*;

public class EmailSender {

    private static final String USERNAME = "oleg2812POA2006@gmail.com";
    private static final String PASSWORD = "xkox eqpd shfv loae"; // Пароль додатка (16 символів)
    private static final String TO_EMAIL = "oleg2812POA2006@gmail.com"; // Куди слати звіт

    public static void sendFatalError(String errorMessage, Throwable error) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); // TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(TO_EMAIL)
            );
            message.setSubject("CRITICAL ERROR in HomeApp: " + errorMessage);
            StringBuilder body = new StringBuilder();
            body.append("Сталася фатальна помилка в програмі.\n\n");
            body.append("Повідомлення: ").append(errorMessage).append("\n");
            body.append("Деталі:\n");

            if (error != null) {
                for (StackTraceElement element : error.getStackTrace()) {
                    body.append(element.toString()).append("\n");
                }
            }

            message.setText(body.toString());

            Transport.send(message);

            System.out.println("Email з помилкою відправлено адміністратору.");

        } catch (MessagingException e) {
            System.err.println("Не вдалося відправити Email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
