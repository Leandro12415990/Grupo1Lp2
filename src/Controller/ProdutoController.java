package Controller;

import BLL.ProdutoBLL;
import Model.Produto;
import Model.ResultadoOperacao;
import View.ProdutoView;

import java.util.List;

public class ProdutoController {
    private final ProdutoBLL produtoBLL;

    public ResultadoOperacao criarProduto(int idProduto,int estado, String nome, String descricao) {
        this.produtoBLL = produtoBLL;
    }

    public ResultadoOperacao criarProduto(int idProduto, int estado, String nome, String descricao) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        if (nome == null || nome.isEmpty()) {
            resultado.msgErro = "O nome do produto é de preenchimento obrigatório!";
        } else if (descricao == null || descricao.isEmpty()) {
            resultado.msgErro = "A descrição do produto é de preenchimento obrigatório!";
        } else {
            Produto novoProduto = new Produto(idProduto, estado, nome, descricao);
            produtoBLL.adicionarProduto(novoProduto);

            resultado.Objeto = resultado;
            resultado.Sucesso = true;
        }
        return resultado;
    }

    public boolean editarProduto(int idProduto, String nome, String descricao, int idEstado) {
        return produtoBLL.editarProduto(idProduto, nome, descricao, idEstado);
    }

    public boolean eliminarProduto(Produto produto) {
        boolean sucesso = produtoBLL.eliminarProduto(produto);

        if (sucesso) {
            produtoBLL.obterTodosProdutos();
        }
        return sucesso;
    }

    public ResultadoOperacao listarProduto(boolean apenasDisponiveis) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        List<Produto> produtos = produtoBLL.listarProdutos(apenasDisponiveis);
        if (produtos.isEmpty()) {
            resultado.msgErro = "Não existem produtos disponíveis para leiloar!";
        } else {
            resultado.Sucesso = true;
            resultado.Objeto = produtos;
        }
        return resultado;
    }

    public Produto procurarProduto(int id) {
        if (id > 0) {
            return produtoBLL.procurarProduto(id);
        }
        return null;
    }

    public String getNomeProdutoById(int idProduto) {
        return produtoBLL.getNomeProdutoById(idProduto);
    }

    public void atualizarEstadoProduto(int idProduto, int novoIdEstado) {
        produtoBLL.atualizarEstadoProduto(idProduto, novoIdEstado);
    }

    public boolean verificarProdutoEmLeilao(int idProduto) {
        return produtoBLL.verificarProdutoEmLeilao(idProduto);
    }
}
