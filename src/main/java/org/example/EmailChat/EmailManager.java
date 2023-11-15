package org.example.EmailChat;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class EmailManager {
    public static void EmailSend(Information information,SenderInformation senderInformation) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");

        Authenticator authenticator1 = new Authenticator() {

        protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(senderInformation.getSenderEmail(),
                senderInformation.getToken());
        }
        };
        Session session = Session.getInstance(properties, authenticator1);
        MimeMessage massage = new MimeMessage(session);
        massage.setFrom(new InternetAddress(senderInformation.getSenderEmail()));
        massage.setRecipient(Message.RecipientType.TO, new InternetAddress(information.getRecipient()));
        massage.setSubject(information.getSubject());
        massage.setText(information.getMassage());
        Transport.send(massage);

    }
}
