package com.store.notification.service.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Map;

@Service
public class EmailServiceImplements extends GenericServiceImplements {
    @Autowired // Injeção de dependência em uma variável. Não precisa passar a construção
    private JavaMailSender mailSender;

    //@Value("${spring.mail.username}")
    @Value("${spring.mail.username}")
    private String mailFrom;

    public Map<String, Object> convertToObjet(String jasonS) {
        try {
            ObjectMapper objectMapper =  new ObjectMapper();
            Map<String, Object> map = objectMapper.readValue(jasonS, Map.class);
            return map;
        } catch (JsonProcessingException e) {
            //throw new RuntimeException(e);
            return null;
        }
    }

    public String constructOrderContent(String products_name, String name) {
        return MessageFormat.format("<body><header><h1>Olá, {0}!</h1><p><h2>Obrigados por comprar nosso(s) produto(s).</h2></p><header><body>", name, products_name);

    }

    // Envia o e-mail para o usuário
    public void sendEmail(String content, String email, String subject) {
        //SimpleMailMessage message = new SimpleMailMessage();
        //message.setFrom(mailFrom); // Quem está enviando o e-mail
        //message.setTo(email); // Quem está recebendo o e-mail
        //message.setSubject(subject); // Título do e-mail
        //message.setText(content); // Conteúdo do e-mail

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(mailFrom); // Quem está enviando o e-mail
            helper.setTo(email); // Quem está recebendo o e-mail
            helper.setSubject(subject); // Título do e-mail
            helper.setText(content, true); // Conteúdo do e-mail

            // Objeto que faz o envio do e-mail
            mailSender.send(message);
        } catch (MessagingException e) {
            //throw new RuntimeException(e);
            System.out.println("Erro: " + e);
        }
    }
}