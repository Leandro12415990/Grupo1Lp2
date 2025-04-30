package Model;

public class Classificacao {
    private int idLeilao;
    private int idCLiente;
    private int classificacao;
    private String comentario;

    public Classificacao(int idLeilao, int idCLiente, int classificacao, String comentario) {
        this.idLeilao = idLeilao;
        this.idCLiente = idCLiente;
        this.classificacao = classificacao;
        this.comentario = comentario;
    }

    public int getIdLeilao() {
        return idLeilao;
    }

    public void setIdLeilao(int idLeilao) {
        this.idLeilao = idLeilao;
    }

    public int getIdCLiente() {
        return idCLiente;
    }

    public void setIdCLiente(int idCLiente) {
        this.idCLiente = idCLiente;
    }

    public int getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(int classificacao) {
        this.classificacao = classificacao;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return "Classificacao{" +
                "idLeilao=" + idLeilao +
                ", idCLiente=" + idCLiente +
                ", classificacao=" + classificacao +
                ", comentario='" + comentario + '\'' +
                '}';
    }
}
