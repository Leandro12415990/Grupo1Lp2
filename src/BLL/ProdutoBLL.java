package BLL;

import DAL.ProdutoDAL;
import Model.Leilao;
import Model.Produto;
import Utils.Constantes;

import java.util.ArrayList;
import java.util.List;

public class ProdutoBLL {
    private final ProdutoDAL produtoDal;
    private final LeilaoBLL leilaoBLL;

    public ProdutoBLL(ProdutoDAL produtoDal, LeilaoBLL leilaoBLL) {
        this.produtoDal = produtoDal;
        this.leilaoBLL = leilaoBLL;
    }

    public List<Produto> carregarProdutos() {
        List<Produto> produtos = produtoDal.carregarProdutos();
        int idEstado;
        for (Produto produto : produtos) {
            idEstado = determinarEstadoProduto(produto);
            produto.setEstado(idEstado);
            produtoDal.gravarProdutos(produtos);
        }
        return produtos;
    }

    public void adicionarProduto(Produto produto) {
        List<Produto> produtos = carregarProdutos();
        produto.setIdProduto(verificarUltimoId(produtos) + 1);
        produtos.add(produto);
        produtoDal.gravarProdutos(produtos);
    }

    public List<Produto> obterTodosProdutos() {
        return produtoDal.carregarProdutos();
    }

    public List<Produto> listarProdutos(boolean apenasDisponiveis) {
        List<Produto> produtos = carregarProdutos();
        if (!apenasDisponiveis) return produtos;
        List<Produto> produtosAtivos = new ArrayList<>();
        for (Produto produto : produtos) {
            if (produto.getEstado() == 1) produtosAtivos.add(produto);
        }
        return produtosAtivos;
    }

    public Produto procurarProduto(int id) {
        List<Produto> produtos = obterTodosProdutos();
        for (Produto produto : produtos) {
            if (produto.getIdProduto() == id) return produto;
        }
        return null;
    }

    private int verificarUltimoId(List<Produto> produtos) {
        int ultimoId = 0;
        for (Produto produto : produtos) {
            if (produto.getIdProduto() > ultimoId) ultimoId = produto.getIdProduto();
        }
        return ultimoId;
    }

    public boolean editarProduto(int idProduto, String nome, String descricao, int idEstado) {
        Produto produto = procurarProduto(idProduto);
        if (produto != null) {
            produto.setNome(nome);
            produto.setDescricao(descricao);
            produto.setEstado(idEstado);
            produtoDal.gravarProdutos(obterTodosProdutos());
            return true;
        }
        return false;
    }

    public boolean eliminarProduto(Produto produto) {
        List<Produto> produtos = obterTodosProdutos();
        boolean produtoRemovido = false;

        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getIdProduto() == produto.getIdProduto()) {
                produtos.remove(i);
                produtoRemovido = true;
                break;
            }
        }
        if (produtoRemovido) produtoDal.gravarProdutos(produtos);
        return produtoRemovido;
    }

    public String getNomeProdutoById(int idProduto) {
        List<Produto> produtos = carregarProdutos();
        for (Produto produto : produtos) {
            if (produto.getIdProduto() == idProduto) return produto.getNome().toUpperCase();
        }
        return null;
    }

    public boolean verificarDisponibilidadeProduto(int idProduto) {
        List<Produto> produtos = produtoDal.carregarProdutos();
        for (Produto produto : produtos) {
            if (produto.getIdProduto() == idProduto) {
                if (produto.getEstado() == Constantes.estadosProduto.ATIVO) return true;
                else return false;
            }
        }
        return false;
    }

    public void atualizarEstadoProduto(int idProduto, int novoIdEstado) {
        List<Produto> produtos = carregarProdutos();
        for (Produto produto : produtos) {
            if (produto.getIdProduto() == idProduto) produto.setEstado(novoIdEstado);
        }
        produtoDal.gravarProdutos(produtos);
    }

    public int determinarEstadoProduto(Produto produto) {
        if (produto.getEstado() != Constantes.estadosProduto.INATIVO) {
            for (Leilao leilao : leilaoBLL.listarLeiloes(false)) {
                if (leilao.getIdProduto() == produto.getIdProduto()) return Constantes.estadosProduto.RESERVADO;
            }
        }
        return Constantes.estadosProduto.ATIVO;
    }

    public boolean verificarProdutoEmLeilao(int idProduto) {
        for (Leilao leilao : leilaoBLL.listarLeiloes(false)) {
            if (leilao.getIdProduto() == idProduto) return false;
        }
        return true;
    }
}
