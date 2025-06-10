package DAL;

import Model.Email;
import Model.Lance;
import Model.Transacao;
import Utils.Constantes.caminhosFicheiros;
import Utils.DataBaseConnection;
import Utils.Tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class EmailDAL {

    public List<Email> carregarEmailsCSV() {
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

    public void gravarEmailsCSV(List<Email> emails) {
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

    public List<Email> carregarEmails() {

        List<Email> listaEmail = new ArrayList<>();
        String sql = "select * from Email";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                int idEmail = rs.getInt("id_Email");
                String fromEmail = rs.getString("From_Email");
                int idCliente = rs.getInt("id_Cliente");
                String toEmail = rs.getString("To_Email");
                String subject = rs.getString("Subject");
                String body = rs.getString("Body");

                // Converte java.sql.Date para java.time.LocalDate
                LocalDateTime dateCreated = rs.getTimestamp("DateCreated") != null ? rs.getTimestamp("DateCreated").toLocalDateTime() : null;

                String idTipoEmail = rs.getString("id_Tipo_Email");

                Email email = new Email(idEmail, fromEmail, idCliente, toEmail, subject, body, dateCreated, idTipoEmail);
                listaEmail.add(email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaEmail;
    }

    public void gravarEmails(List<Email> emails) {

        String sqlInsert = "INSERT INTO Transacao (From_Email, id_Cliente, To_Email, Subject, Body, DateCreated, id_Tipo_Email) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        String sqlUpdate = "UPDATE Transacao SET From_Email = ?, id_Cliente = ?, To_Email = ?, Subject = ?, Body = ?, DateCreated = ?, id_Tipo_Email = ? " +
                "WHERE Id_Email = ?";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
                PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
        ) {
            for (Email u : emails) {
                if (u.getIdEmail() == 0) {
                    // INSERT
                    stmtInsert.setString(1, u.getFromEmail());
                    stmtInsert.setInt(2, u.getIdCliente());
                    stmtInsert.setString(4, u.getToEmail());
                    stmtInsert.setString(5, u.getSubject());
                    stmtInsert.setString(8, u.getBody());
                    stmtInsert.setDate(6, u.getDateCreated() != null
                            ? java.sql.Date.valueOf(u.getDateCreated().toLocalDate())
                            : null);
                    stmtInsert.setString(9, u.getIdTipoEmail());

                    stmtInsert.addBatch();
                } else {
                    // UPDATE
                    stmtUpdate.setString(1, u.getFromEmail());
                    stmtUpdate.setInt(2, u.getIdCliente());
                    stmtUpdate.setString(4, u.getToEmail());
                    stmtUpdate.setString(5, u.getSubject());
                    stmtUpdate.setString(8, u.getBody());
                    stmtUpdate.setDate(6, u.getDateCreated() != null
                            ? java.sql.Date.valueOf(u.getDateCreated().toLocalDate())
                            : null);
                    stmtUpdate.setString(9, u.getIdTipoEmail());

                    stmtUpdate.addBatch();
                }
            }

            stmtInsert.executeBatch();
            stmtUpdate.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
