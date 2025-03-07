package Controller;

import BLL.ProdutoBLL;
import Model.Produto;

import java.util.ArrayList;
import java.util.List;

public class ProdutoController {

    public static void criarProduto(String nome, String descricao) {
        if(nome == null || nome.isEmpty() || descricao == null || descricao.isEmpty()) {
            System.out.println("Erro ao criar produto");
            return;
        }
        if (ProdutoBLL.produtoExiste(nome)) {
            System.out.println("Erro: Produto já existe.");
            return;
        }

       Produto novoProduto = new Produto(nome, descricao);
       ProdutoBLL.adicionarProduto(novoProduto);
        System.out.println("Produto criado com sucesso: " + novoProduto.getNome() + " - " + novoProduto.getDescricao());
    }

    public static void listarProduto() {
        ProdutoBLL.listarProdutos();
    }
}
