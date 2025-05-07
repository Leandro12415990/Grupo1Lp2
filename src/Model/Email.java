package Model;

import java.time.LocalDateTime;

public class Email {
    private int idEmail;
    private String fromEmail;
    private int idCliente;
    private String toEmail;
    private String subject;
    private String body;
    private LocalDateTime dateCreated;
    private String idTipoEmail;

    public Email(int idEmail, String fromEmail, int idCliente, String toEmail, String subject, String body, LocalDateTime dateCreated, String idTipoEmail) {
        this.idEmail = idEmail;
        this.fromEmail = fromEmail;
        this.idCliente = idCliente;
        this.toEmail = toEmail;
        this.subject = subject;
        this.body = body;
        this.dateCreated = dateCreated;
        this.idTipoEmail = idTipoEmail;
    }

    public int getIdEmail() {
        return idEmail;
    }

    public void setIdEmail(int idEmail) {
        this.idEmail = idEmail;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getIdTipoEmail() {
        return idTipoEmail;
    }

    public void setIdTipoEmail(String idTipoEmail) {
        this.idTipoEmail = idTipoEmail;
    }
}
