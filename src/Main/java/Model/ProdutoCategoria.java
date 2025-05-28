package Model;

public class ProdutoCategoria {
    private int idProduto;
    private int idCategoria;

    public ProdutoCategoria(int idProduto, int idCategoria) {
        this.idProduto = idProduto;
        this.idCategoria = idCategoria;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    @Override
    public String toString() {
        return "ProdutoCategoria{" +
                "idProduto=" + idProduto +
                ", idCategoria=" + idCategoria +
                '}';
    }
}
