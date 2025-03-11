package Controller;

import BLL.ProdutoBLL;
import Model.Produto;
import Utils.Tools;

import java.util.ArrayList;
import java.util.List;

public class ProdutoController {

    public static void criarProduto(String nome, String descricao) {
        if(nome == null || nome.isEmpty() || descricao == null || descricao.isEmpty()) {
            System.out.println("Erro ao criar produto");
            return;
        }

       Produto novoProduto = new Produto(nome, descricao);
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

    public static void eliminarProduto(int id) {
        // 🔹 1. Verificar se o ID é válido
        if (id <= 0) {
            System.out.println("[ERRO] O ID deve ser um número positivo.");
            return;
        }

        // 🔹 2. Obter a lista de produtos da BLL
        List<Produto> produtos = ProdutoBLL.obterTodosProdutos();
        Produto produtoEncontrado = null;

        // 🔹 3. Verificar se o produto existe
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

        // 🔹 4. Confirmar com o utilizador antes de eliminar
        System.out.println("⚠️ Produto encontrado: " + produtoEncontrado);
        System.out.print("Tem certeza que deseja apagar este produto? (S/N): ");
        String resposta = Tools.scanner.nextLine().trim().toUpperCase();

        if (!resposta.equals("S")) {
            System.out.println("❌ Eliminação cancelada.");
            return;
        }

        // 🔹 5. Se confirmou, pedir à BLL para eliminar o produto
        ProdutoBLL.eliminarProduto(produtoEncontrado);
    }

    public static void listarProduto() {
        ProdutoBLL.listarProdutos();
    }
}
