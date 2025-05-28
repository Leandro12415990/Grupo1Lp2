package BLL;

import DAL.CategoriaDAL;
import DAL.ProdutoCategoriaDAL;
import Model.Categoria;
import Model.ProdutoCategoria;
import Utils.Constantes;

import java.util.List;

public class ProdutoCategoriaBLL {
    private ProdutoCategoriaDAL dal = new ProdutoCategoriaDAL();
    private CategoriaBLL categoriaBLL = new CategoriaBLL();

    public boolean associarCategoria(ProdutoCategoria produtoCategoria) {
        Categoria categoria = categoriaBLL.procurarCategoria(produtoCategoria.getIdCategoria());
        if (categoria != null && categoria.getEstado() == Constantes.estadosCategoria.INATIVO) {
            boolean estadoAlterado = categoriaBLL.atualizarCategoriaEstado(categoria.getIdCategoria(), Constantes.estadosCategoria.ATIVO);
            if (!estadoAlterado) {
                System.out.println("Erro ao alterar o estado da categoria.");
                return false;
            }
        }

        List<ProdutoCategoria> associacoes = dal.carregarProdutoCategoria();
        associacoes.add(produtoCategoria);
        dal.gravarProdutoCategoria(associacoes);

        return true;
    }

    public boolean desassociarCategoriaDoProduto(int idProduto) {
        ProdutoCategoriaDAL produtoCategoriaDAL = new ProdutoCategoriaDAL();
        List<ProdutoCategoria> produtoCategorias = produtoCategoriaDAL.carregarProdutoCategoria();

        ProdutoCategoria produtoCategoriaASerRemovido = null;
        for (ProdutoCategoria produtoCategoria : produtoCategorias) {
            if (produtoCategoria.getIdProduto() == idProduto) {
                produtoCategoriaASerRemovido = produtoCategoria;
                break;
            }
        }

        if (produtoCategoriaASerRemovido == null) {
            return false;
        }

        produtoCategorias.remove(produtoCategoriaASerRemovido);
        int idCategoria = produtoCategoriaASerRemovido.getIdCategoria();
        boolean categoriaTemProdutoAssociado = false;
        for (ProdutoCategoria produtoCategoria : produtoCategorias) {
            if (produtoCategoria.getIdCategoria() == idCategoria) {
                categoriaTemProdutoAssociado = true;
                break;
            }
        }

        if (!categoriaTemProdutoAssociado) {
            CategoriaBLL categoriaBLL = new CategoriaBLL();
            categoriaBLL.atualizarCategoriaEstado(idCategoria, Constantes.estadosCategoria.INATIVO);
        }

        produtoCategoriaDAL.gravarProdutoCategoria(produtoCategorias);

        return true;
    }

    public boolean verificarCategoriaSemProdutoAssociado(int idCategoria) {
        ProdutoCategoriaDAL produtoCategoriaDAL = new ProdutoCategoriaDAL();
        List<ProdutoCategoria> produtoCategorias = produtoCategoriaDAL.carregarProdutoCategoria();

        for (ProdutoCategoria produtoCategoria : produtoCategorias) {
            if (produtoCategoria.getIdCategoria() == idCategoria) {
                return false;
            }
        }

        return true;
    }

    public ProdutoCategoria procurarCategoriaPorProduto(int idProduto) {
        ProdutoCategoriaDAL produtoCategoriaDAL = new ProdutoCategoriaDAL();
        List<ProdutoCategoria> produtoCategorias = produtoCategoriaDAL.carregarProdutoCategoria();

        for (ProdutoCategoria produtoCategoria : produtoCategorias) {
            if (produtoCategoria.getIdProduto() == idProduto) {
                return produtoCategoria;
            }
        }

        return null;
    }


}
