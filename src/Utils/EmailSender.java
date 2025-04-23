package Utils;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import static Utils.Constantes.configEmail;

public class EmailSender {

    public static boolean enviarEmailSimples(String nomeDestinatario, String emailDestinatario, String assunto, String mensagem) {
        try {
            Email email = EmailBuilder.startingBlank()
                    .from(configEmail.fromName, configEmail.fromEmail)
                    .to(nomeDestinatario, emailDestinatario.toLowerCase())
                    .withSubject(assunto)
                    .withPlainText(mensagem)
                    .buildEmail();

            Mailer mailer = MailerBuilder
                    .withSMTPServer(configEmail.host, configEmail.portSimple, configEmail.fromEmail, configEmail.keyPassword)
                    .withTransportStrategy(TransportStrategy.SMTP_TLS)
                    .buildMailer();

            mailer.sendMail(email);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void enviarEmailHtml(String nomeDestinatario,String emailDestinatario, String assunto, String htmlMensagem) {
        Email email = EmailBuilder.startingBlank()
                .from(configEmail.fromName, configEmail.fromEmail)
                .to(nomeDestinatario, emailDestinatario.toLowerCase())
                .withSubject(assunto)
                .withHTMLText(htmlMensagem)
                .buildEmail();

        Mailer mailer = MailerBuilder
                .withSMTPServer(configEmail.host, configEmail.portSimple, configEmail.fromEmail, configEmail.keyPassword)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();

        mailer.sendMail(email);
    }

}
