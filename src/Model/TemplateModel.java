package Model;

public class TemplateModel {
    private String assunto;
    private String corpo;

    public TemplateModel(String assunto, String corpo) {
        this.assunto = assunto;
        this.corpo = corpo;
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
