package Controller;

import BLL.ProdutoBLL;
import Model.Produto;

import java.util.List;

public class ProdutoController {

    public static void criarProduto(int idProduto, String nome, String descricao) {
        if(nome == null || nome.isEmpty() || descricao == null || descricao.isEmpty()) {
            System.out.println("Erro ao criar produto");
            return;
        }

        Produto novoProduto = new Produto(idProduto,nome, descricao);
        ProdutoBLL.adicionarProduto(novoProduto);
        System.out.println("Produto criado com sucesso: " + novoProduto.getNome() + " - " + novoProduto.getDescricao());
    }

    public static void editarProduto(int id) {
        if (id <= 0) {
            System.out.println("[ERRO] O ID deve ser um número positivo.");
            return;
        }

        List<Produto> produtos = ProdutoBLL.obterTodosProdutos();
        Produto produtoEncontrado = null;

        for (Produto produto : produtos) {
            if (produto.getIdProduto() == id) {
                produtoEncontrado = produto;
                break;
            }
        }

        if (produtoEncontrado == null) {
            System.out.println("[ERRO] Produto com ID " + id + " não encontrado.");
            return;
        }

        ProdutoBLL.editarProduto(produtoEncontrado);
    }

    public static boolean eliminarProduto(Produto produto) {
        ProdutoBLL.eliminarProduto(produto);
        return true;
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
}
