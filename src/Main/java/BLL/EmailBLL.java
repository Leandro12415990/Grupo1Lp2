package BLL;

import DAL.EmailDAL;
import DAL.TemplateDAL;
import Model.Email;
import Model.Template;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static Utils.Constantes.configEmail;

public class EmailBLL {

    public void enviarEmail(String templateId, String toEmail, Map<String, String> variaveis, int idCliente)
            throws IOException, MessagingException {
        TemplateDAL templateDAL = new TemplateDAL();
        Template template = templateDAL.carregarTemplatePorId(templateId);
        enviarEmail(template, toEmail, variaveis, idCliente);
    }

    public void enviarEmailComAnexo(Template template, String toEmail, Map<String, String> variaveis, int idCliente, String caminhoAnexo)
            throws MessagingException {

        String assunto = substituirTags(template.getAssunto(), variaveis);
        String corpo = substituirTags(template.getCorpo(), variaveis);

        Session session = criarSessaoEmail();
        Message message = criarMensagemComAnexo(session, toEmail, assunto, corpo, idCliente, template.getId(), caminhoAnexo);

        Transport.send(message);
    }

    private Message criarMensagemComAnexo(Session session, String toEmail, String assunto, String corpo, int idCliente, String idTipoEmail, String caminhoAnexo)
            throws MessagingException {

        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(configEmail.fromEmail, configEmail.fromName));
        } catch (UnsupportedEncodingException e) {
            message.setFrom(new InternetAddress(configEmail.fromEmail));
        }

        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(assunto);

        // Parte do corpo do e-mail
        MimeBodyPart corpoParte = new MimeBodyPart();
        corpoParte.setText(corpo, "utf-8");

        // Parte do anexo
        MimeBodyPart anexoParte = new MimeBodyPart();
        try {
            anexoParte.attachFile(caminhoAnexo); // caminho absoluto ou relativo ao ficheiro
        } catch (IOException e) {
            throw new MessagingException("Erro ao anexar ficheiro: " + caminhoAnexo, e);
        }

        // Juntar tudo num Multipart
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(corpoParte);
        multipart.addBodyPart(anexoParte);

        message.setContent(multipart);

        // Registo do e-mail enviado
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
        message.setText(corpo);

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

    public boolean foiEmailAvisoEnviado(int idUtilizador, String tipoAviso) {
        return existeEnvioAnterior(idUtilizador, tipoAviso);
    }

    public boolean existeEnvioAnterior(int idUtilizador, String tipoAviso) {
        List<Email> emails = new EmailDAL().carregarEmails();
        for (Email email : emails) {
            if (email.getIdCliente() == idUtilizador && email.getIdTipoEmail().equals(tipoAviso)) {
                return true;
            }
        }
        return false;
    }
}