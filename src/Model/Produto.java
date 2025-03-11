package Model;

public class Produto {
    private static int contador = 1;
    private int idProduto;
    private String nome;
    private String descricao;

    public Produto(String nome, String descricao) {
        this.idProduto = contador++;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Produto(int idProduto, String nome, String descricao) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.descricao = descricao;

    }

    public static void atualizarContador(int maiorId) {
        if (maiorId >= contador) {
            contador = maiorId + 1;
        }
    }

    public int getIdProduto() {
        return idProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String toString() {
        return "ID: " + idProduto + " - Nome: " + nome + " - Descrição: " + descricao;
    }
}
