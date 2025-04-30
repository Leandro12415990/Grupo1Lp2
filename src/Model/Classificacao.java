package Model;

public class Classificacao {
    private int idLeilao;
    private int idUtilizador;
    private int classificacao;
    private String comentario;

    public Classificacao(int idLeilao, int idUtilizador,int classificacao, String comentario) {
        this.idLeilao = idLeilao;
        this.idUtilizador = idUtilizador;
        this.classificacao = classificacao;
        this.comentario = comentario;
    }

    public int getIdLeilao() {
        return idLeilao;
    }

    public void setIdLeilao(int idLeilao) {
        this.idLeilao = idLeilao;
    }

    public int getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(int idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(int classificacao) {
        this.classificacao = classificacao;
    }

    @Override
    public String toString() {
        return "Classificacao{" +
                "idLeilao=" + idLeilao +
                ", idUtilizador=" + idUtilizador +
                ", classificacao=" + classificacao +
                ", comentario='" + comentario + '\'' +
                '}';
    }
}
