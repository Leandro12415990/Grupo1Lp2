package Controller;

import BLL.ProdutoBLL;
import Model.Produto;

import java.util.List;

public class ProdutoController {

    public static void criarProduto(int idProduto,int estado, String nome, String descricao) {
        if(nome == null || nome.isEmpty() || descricao == null || descricao.isEmpty()) {
            System.out.println("Erro ao criar produto");
            return;
        }

        Produto novoProduto = new Produto(idProduto,estado,nome, descricao);
        ProdutoBLL.adicionarProduto(novoProduto);
        System.out.println("Produto criado com sucesso: " + novoProduto.getNome() + " - " + novoProduto.getDescricao());
    }

    public static boolean editarProduto(Produto produto) {
        boolean sucesso = ProdutoBLL.editarProduto(produto);

        if(sucesso) {
            ProdutoBLL.obterTodosProdutos();
        }
        return sucesso;
    }

    public static boolean eliminarProduto(Produto produto) {
        boolean sucesso = ProdutoBLL.eliminarProduto(produto);


        if (sucesso) {
            ProdutoBLL.obterTodosProdutos();
        }
        return sucesso;
    }

    public static void listarProduto() {
        ProdutoBLL.listarProdutos();
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
}
