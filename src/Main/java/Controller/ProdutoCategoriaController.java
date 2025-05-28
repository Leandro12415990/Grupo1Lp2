package Controller;

import BLL.ProdutoCategoriaBLL;
import Model.ProdutoCategoria;

public class ProdutoCategoriaController {
    private ProdutoCategoriaBLL bll = new ProdutoCategoriaBLL();

    public boolean associarCategoriaAoProduto(int idProduto, int idCategoria) {
        ProdutoCategoria produtoCategoria = new ProdutoCategoria(idProduto, idCategoria);
        return bll.associarCategoria(produtoCategoria);
    }

    public boolean desassociarCategoriaDoProduto(int idProduto) {
        ProdutoCategoriaBLL bll = new ProdutoCategoriaBLL();
        return bll.desassociarCategoriaDoProduto(idProduto);
    }

    public boolean verificarCategoriaSemProdutoAssociado(int idCategoria) {
        ProdutoCategoriaBLL bll = new ProdutoCategoriaBLL();
        return bll.verificarCategoriaSemProdutoAssociado(idCategoria);
    }

        public ProdutoCategoria procurarCategoriaPorProduto(int idProduto) {
            return bll.procurarCategoriaPorProduto(idProduto);
        }
    }

