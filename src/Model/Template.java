package Model;

public class Template {
    private int idTemplate;
    private String nome;
    private String conteudo;

    public Template(int idTemplate, String nome, String conteudo) {
        this.idTemplate = idTemplate;
        this.nome = nome;
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

    public String getConteudo() { return conteudo; }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    @Override
    public String toString() {
        return "[" + idTemplate + "] " + nome + ": " + conteudo;
    }
}

