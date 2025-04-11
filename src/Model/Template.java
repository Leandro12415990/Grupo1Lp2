package Model;

public class Template {
    private int idTemplate;
    private String nome;
    private String assunto;
    private String conteudo;

    public Template(int idTemplate, String nome, String assunto, String conteudo) {
        this.idTemplate = idTemplate;
        this.nome = nome;
        this.assunto = assunto;
        this.conteudo = conteudo;
    }

    public int getIdTemplate() { return idTemplate; }

    public void setIdTemplate(int idTemplate) {
        this.idTemplate = idTemplate;
    }

    public String getNome() { return nome; }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getConteudo() { return conteudo; }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    @Override
    public String toString() {
        return "[" + idTemplate + "] " + nome + ": " + conteudo;
    }
}

