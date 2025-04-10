package Utils;

import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.api.mailer.config.TransportStrategy;

public class EmailSender {

    public static void enviarEmailSimples(String destinatario, String assunto, String mensagem) {
        Email email = EmailBuilder.startingBlank()
                .from("Pedro Pereira", "pedro.pereira.25790@gmail.com") // substitui pelos teus dados
                .to("Destinatário", destinatario)
                .withSubject(assunto)
                .withPlainText(mensagem)
                .buildEmail();

        Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, "pedro.pereira.25790@gmail.com", "wlqw hyyi knrf nfkf")
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();

        mailer.sendMail(email);
    }
}
