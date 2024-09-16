package com.example.config.email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendConfirmationEmail(String to, String token) {
        String confirmationUrl = createConfirmationUrl(token);
        String subject = "Confirma tu registro";
        String text = "Gracias por registrarte. Por favor, confirma tu registro haciendo clic en el siguiente enlace: " + confirmationUrl;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    private String createConfirmationUrl(String token) {
        return "http://localhost:5000/confirm?token=" + token;
    }
}