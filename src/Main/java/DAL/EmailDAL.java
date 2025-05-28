package DAL;

import Model.Email;
import Utils.Constantes.caminhosFicheiros;
import Utils.Tools;

import java.time.LocalDateTime;
import java.util.List;


public class EmailDAL {

    public List<Email> carregarEmails() {
        ImportDAL importDal = new ImportDAL();
        return importDal.carregarRegistos(caminhosFicheiros.CSV_FILE_EMAIL, 8, dados -> {
            int idEmail = Integer.parseInt(dados[0]);
            String fromEmail = dados[1];
            int idCliente = Integer.parseInt(dados[2]);
            String toEmail = dados[3];
            String subject = dados[4];
            String body = dados[5];
            LocalDateTime dateCreated = Tools.parseDateTimeByDate(dados[6]);
            String idTipoEmail = dados[7];
            return new Email(idEmail, fromEmail, idCliente, toEmail, subject, body, dateCreated, idTipoEmail);
        });
    }

    public void gravarEmails(List<Email> emails) {
        ImportDAL importDal = new ImportDAL();
        String cabecalho = "ID_EMAIL;FROM_EMAIL;ID_CLIENTE;TO_EMAIL;SUBJECT;BODY;DATE_CREATED;ID_TIPO_EMAIL";
        importDal.gravarRegistos(caminhosFicheiros.CSV_FILE_EMAIL, cabecalho, emails, email ->
                email.getIdEmail() + Tools.separador() +
                        email.getFromEmail() + Tools.separador() +
                        email.getIdCliente() + Tools.separador() +
                        email.getToEmail() + Tools.separador() +
                        email.getSubject() + Tools.separador() +
                        email.getBody()
                                .replace("\r", "\\r")
                                .replace("\n", "\\n") + Tools.separador() +
                        Tools.formatDateTime(email.getDateCreated()) + Tools.separador() +
                        email.getIdTipoEmail()
        );
    }

}
