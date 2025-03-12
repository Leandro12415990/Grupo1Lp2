package Model;

public class Produto {
    private int idProduto;
    private String nome;
    private String descricao;

    public Produto(int idProduto,String nome, String descricao) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.descricao = descricao;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
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


}
