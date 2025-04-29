package BLL;

import DAL.TemplateDAL;
import Model.TemplateModel;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.IOException;
import java.util.*;

import static Utils.Constantes.configEmail;

public class EmailBLL {

    public void enviarEmail(String templateId, String toEmail, Map<String, String> variaveis)
            throws IOException, MessagingException {
        TemplateDAL templateDAL = new TemplateDAL();
        TemplateModel template = templateDAL.carregarTemplatePorId(templateId);
        enviarEmail(template, toEmail, variaveis);
    }

    public void enviarEmail(TemplateModel template, String toEmail, Map<String, String> variaveis)
            throws MessagingException {

        String assunto = substituirTags(template.getAssunto(), variaveis);
        String corpo = substituirTags(template.getCorpo(), variaveis);

        Session session = criarSessaoEmail();

        Message message = criarMensagemEmail(session, toEmail, assunto, corpo);

        Transport.send(message);
    }

    private Session criarSessaoEmail() {
        Properties props = new Properties();
        props.put("mail.smtp.host", configEmail.host);
        props.put("mail.smtp.port", configEmail.port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(configEmail.fromEmail, configEmail.keyPassword);
            }
        });
    }

    private Message criarMensagemEmail(Session session, String toEmail, String assunto, String corpo)
            throws MessagingException {
        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(configEmail.fromEmail, configEmail.fromName));
        } catch (java.io.UnsupportedEncodingException e) {
            message.setFrom(new InternetAddress(configEmail.fromEmail));
        }

        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(assunto);
        message.setContent(corpo, "text/html; charset=UTF-8"); // <-- HTML aqui

        return message;
    }

    private String substituirTags(String texto, Map<String, String> variaveis) {
        for (Map.Entry<String, String> entry : variaveis.entrySet()) {
            texto = texto.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return texto;
    }
}