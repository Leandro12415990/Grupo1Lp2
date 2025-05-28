package Controller;

import BLL.ProdutoBLL;
import DAL.ProdutoCategoriaDAL;
import Model.Produto;
import Model.ProdutoCategoria;
import Model.ResultadoOperacao;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.List;

public class ProdutoController {
    public ResultadoOperacao criarProduto(int idProduto, int estado, String nome, String descricao) throws MessagingException, IOException {
        ProdutoBLL produtoBLL = new ProdutoBLL();
        ResultadoOperacao resultado = new ResultadoOperacao();

        if (nome == null || nome.isEmpty()) {
            resultado.msgErro = "O nome do produto é de preenchimento obrigatório!";
            resultado.Sucesso = false;
        } else if (descricao == null || descricao.isEmpty()) {
            resultado.msgErro = "A descrição do produto é de preenchimento obrigatório!";
            resultado.Sucesso = false;
        } else {
            Produto novoProduto = new Produto(idProduto, estado, nome, descricao);
            produtoBLL.adicionarProduto(novoProduto);

            resultado.Objeto = novoProduto;  // <<< CORREÇÃO AQUI
            resultado.Sucesso = true;
        }
        return resultado;
    }

    public boolean editarProduto(int idProduto, String nome, String descricao, int idEstado) {
        ProdutoBLL produtoBLL = new ProdutoBLL();
        return produtoBLL.editarProduto(idProduto, nome, descricao, idEstado);
    }

    public boolean eliminarProduto(Produto produto) {
        ProdutoBLL produtoBLL = new ProdutoBLL();
        boolean sucesso = produtoBLL.eliminarProduto(produto);

        if (sucesso) produtoBLL.obterTodosProdutos();
        return sucesso;
    }

    public List<Produto> listarProduto(boolean apenasDisponiveis) throws MessagingException, IOException {
        ProdutoBLL produtoBLL = new ProdutoBLL();
        return produtoBLL.listarProdutos(apenasDisponiveis);
    }

    public Produto procurarProduto(int id) {
        ProdutoBLL produtoBLL = new ProdutoBLL();
        if (id > 0) return produtoBLL.procurarProduto(id);
        return null;
    }

    public String getNomeProdutoById(int idProduto) throws MessagingException, IOException {
        ProdutoBLL produtoBLL = new ProdutoBLL();
        return produtoBLL.getNomeProdutoById(idProduto);
    }

    public void atualizarEstadoProduto(int idProduto, int novoIdEstado) throws MessagingException, IOException {
        ProdutoBLL produtoBLL = new ProdutoBLL();
        produtoBLL.atualizarEstadoProduto(idProduto, novoIdEstado);
    }

    public boolean verificarProdutoEmLeilao(int idProduto) throws MessagingException, IOException {
        ProdutoBLL produtoBLL = new ProdutoBLL();
        return produtoBLL.verificarProdutoEmLeilao(idProduto);
    }
}
