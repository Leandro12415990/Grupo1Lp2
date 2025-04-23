package BLL;

import Model.TemplateModel;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.*;

public class EmailBLL {

    public void enviarEmail(TemplateModel template, String toEmail, Map<String, String> variaveis) {
        final String fromEmail = "projeto.lp2.v2@gmail.com";
        final String password = "spyt jzsy lylo yzeg";

        // Substituir variáveis no texto
        String assunto = substituirTags(template.getAssunto(), variaveis);
        String corpo = substituirTags(template.getCorpo(), variaveis);

        // Configuração do envio
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(assunto);
            message.setText(corpo);

            Transport.send(message);
            System.out.println("E-mail enviado com sucesso!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String substituirTags(String texto, Map<String, String> variaveis) {
        for (Map.Entry<String, String> entry : variaveis.entrySet()) {
            texto = texto.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return texto;
    }
}
