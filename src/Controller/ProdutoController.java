package Controller;

import BLL.ProdutoBLL;
import Model.Produto;
import Model.ResultadoOperacao;
import View.ProdutoView;

import java.util.List;

public class ProdutoController {

    public ResultadoOperacao criarProduto(int idProduto,int estado, String nome, String descricao) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        if (nome == null || nome.isEmpty()) {
            resultado.msgErro = "O nome do produto é de preenchimento obrigatório!";
        } else if (descricao == null || descricao.isEmpty()) {
            resultado.msgErro = "A descrição do produto é de preenchimento obrigatório!";
        } else {
            Produto novoProduto = new Produto(idProduto, estado, nome, descricao);
            ProdutoBLL.adicionarProduto(novoProduto);

            resultado.Objeto = resultado;
            resultado.Sucesso = true;
        }
        return resultado;
    }

    public static boolean editarProduto(int idProduto, String nome, String descricao, int idEstado) {
        return ProdutoBLL.editarProduto(idProduto, nome, descricao, idEstado);
    }

    public static boolean eliminarProduto(Produto produto) {

        boolean sucesso = ProdutoBLL.eliminarProduto(produto);


        if (sucesso) {
            ProdutoBLL.obterTodosProdutos();
        }
        return sucesso;
    }

    public static ResultadoOperacao listarProduto(boolean apenasDisponiveis) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        List<Produto> produtos = ProdutoBLL.listarProdutos(apenasDisponiveis);
        if(produtos.isEmpty()) {
            resultado.msgErro = "Não existem produtos disponíveis para leiloar!";
        } else {
            ProdutoView.exibirProduto(produtos);
            resultado.Sucesso = true;
            resultado.Objeto = resultado;
        }
        return resultado;
    }

    public static Produto procurarProduto(int Id) {
        if (Id > 0) {
            return ProdutoBLL.procurarProduto(Id);
        }
        return null;
    }

    public static String getNomeProdutoById(int idProduto) {
        return ProdutoBLL.getNomeProdutoById(idProduto);
    }

    public static void atualizarEstadoProduto(int idProduto, int novoIdEstado) {
        ProdutoBLL.atualizarEstadoProduto(idProduto, novoIdEstado);
    }

    public static boolean verificarProdutoEmLeilao(int idProduto) {
        return ProdutoBLL.verificarProdutoEmLeilao(idProduto);
    }
}
