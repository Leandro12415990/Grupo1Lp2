package BLL;

import DAL.EmailDAL;
import DAL.TemplateDAL;
import Model.Email;
import Model.Produto;
import Model.Template;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.*;

import static Utils.Constantes.configEmail;

public class EmailBLL {

    public void enviarEmail(String templateId, String toEmail, Map<String, String> variaveis, int idCliente)
            throws IOException, MessagingException {
        TemplateDAL templateDAL = new TemplateDAL();
        Template template = templateDAL.carregarTemplatePorId(templateId);
        enviarEmail(template, toEmail, variaveis, idCliente);
    }

    public void enviarEmail(Template template, String toEmail, Map<String, String> variaveis, int idCliente)
            throws MessagingException {

        String assunto = substituirTags(template.getAssunto(), variaveis);
        String corpo = substituirTags(template.getCorpo(), variaveis);

        Session session = criarSessaoEmail();

        Message message = criarMensagemEmail(session, toEmail, assunto, corpo, idCliente, template.getId());

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

    private Message criarMensagemEmail(Session session, String toEmail, String assunto, String corpo, int idCliente, String idTipoEmail) throws MessagingException {
        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(configEmail.fromEmail, configEmail.fromName));
        } catch (UnsupportedEncodingException | MessagingException e) {
            message.setFrom(new InternetAddress(configEmail.fromEmail));
        }

        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(assunto);
        message.setContent(corpo, "text/html; charset=UTF-8");

        String corpoTexto = removerTagsHtml(corpo);
        Email email = new Email(
                0,
                configEmail.fromEmail,
                idCliente,
                toEmail,
                assunto,
                corpoTexto,
                LocalDateTime.now(),
                idTipoEmail
        );

        EmailDAL emailDAL = new EmailDAL();
        List<Email> emailsExistentes = emailDAL.carregarEmails();
        email.setIdEmail(verificarUltimoId(emailsExistentes) + 1);
        emailsExistentes.add(email);
        emailDAL.gravarEmails(emailsExistentes);

        return message;
    }

    private String substituirTags(String texto, Map<String, String> variaveis) {
        for (Map.Entry<String, String> entry : variaveis.entrySet()) {
            texto = texto.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return texto;
    }

    private String removerTagsHtml(String html) {
        return html.replaceAll("(?i)<br */?>", "\n")
                .replaceAll("<[^>]+>", "")
                .replaceAll("&nbsp;", " ")
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&")
                .replaceAll("&quot;", "\"")
                .replaceAll("(?m)^[ \t]*\r?\n", "") // remove linhas vazias
                .trim();
    }

    private int verificarUltimoId(List<Email> emails) {
        int ultimoId = 0;
        for (Email email : emails) {
            if (email.getIdEmail() > ultimoId) ultimoId = email.getIdEmail();
        }
        return ultimoId;
    }
}