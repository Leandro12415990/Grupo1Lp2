package Model;

public class Template {
    private String id;
    private String assunto;
    private String corpo;

    public Template(String id, String assunto, String corpo) {
        this.id = id;
        this.assunto = assunto;
        this.corpo = corpo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }
}