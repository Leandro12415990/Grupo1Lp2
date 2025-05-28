package BLL;

import DAL.ProdutoCategoriaDAL;
import DAL.ProdutoDAL;
import Model.Leilao;
import Model.Produto;
import Model.ProdutoCategoria;
import Utils.Constantes;
import Utils.Tools;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoBLL {
    public List<Produto> carregarProdutos() throws MessagingException, IOException {
        ProdutoDAL produtoDal = new ProdutoDAL();
        List<Produto> produtos = produtoDal.carregarProdutos();
        int idEstado;
        for (Produto produto : produtos) {
            idEstado = determinarEstadoProduto(produto);
            produto.setEstado(idEstado);
            produtoDal.gravarProdutos(produtos);
        }
        return produtos;
    }

    public void adicionarProduto(Produto produto) throws MessagingException, IOException {
        ProdutoDAL produtoDal = new ProdutoDAL();
        List<Produto> produtos = carregarProdutos();
        produto.setIdProduto(verificarUltimoId(produtos) + 1);
        produtos.add(produto);
        produtoDal.gravarProdutos(produtos);
    }

    public List<Produto> obterTodosProdutos() {
        ProdutoDAL produtoDal = new ProdutoDAL();
        return produtoDal.carregarProdutos();
    }

    public List<Produto> listarProdutos(boolean apenasDisponiveis) throws MessagingException, IOException {
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
        ProdutoDAL produtoDal = new ProdutoDAL();
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
        ProdutoDAL produtoDal = new ProdutoDAL();
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

    public String getNomeProdutoById(int idProduto) throws MessagingException, IOException {
        List<Produto> produtos = carregarProdutos();
        for (Produto produto : produtos) {
            if (produto.getIdProduto() == idProduto) return produto.getNome().toUpperCase();
        }
        return null;
    }

    public boolean verificarDisponibilidadeProduto(int idProduto) {
        ProdutoDAL produtoDal = new ProdutoDAL();
        List<Produto> produtos = produtoDal.carregarProdutos();
        for (Produto produto : produtos) {
            if (produto.getIdProduto() == idProduto) {
                if (produto.getEstado() == Constantes.estadosProduto.ATIVO) return true;
                else return false;
            }
        }
        return false;
    }

    public void atualizarEstadoProduto(int idProduto, int novoIdEstado) throws MessagingException, IOException {
        ProdutoDAL produtoDal = new ProdutoDAL();
        List<Produto> produtos = carregarProdutos();
        for (Produto produto : produtos) {
            if (produto.getIdProduto() == idProduto) produto.setEstado(novoIdEstado);
        }
        produtoDal.gravarProdutos(produtos);
    }

    public int determinarEstadoProduto(Produto produto) throws MessagingException, IOException {
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        if (produto.getEstado() != Constantes.estadosProduto.INATIVO) {
            for (Leilao leilao : leilaoBLL.listarLeiloes(Tools.estadoLeilao.DEFAULT)) {
                if (leilao.getIdProduto() == produto.getIdProduto()) return Constantes.estadosProduto.RESERVADO;
            }
        }
        return Constantes.estadosProduto.ATIVO;
    }

    public boolean verificarProdutoEmLeilao(int idProduto) throws MessagingException, IOException {
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        for (Leilao leilao : leilaoBLL.listarLeiloes(Tools.estadoLeilao.DEFAULT)) {
            if (leilao.getIdProduto() == idProduto) return false;
        }
        return true;
    }

}
