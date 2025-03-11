package BLL;

import DAL.ImportDal;
import Model.Produto;
import Utils.Tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoBLL {
    private static List<Produto> produtos = new ArrayList<>();

    public static List<Produto> carregarProdutos() {
        produtos = ImportDal.carregarProdutos();
        return produtos;
    }

    public static void adicionarProduto(Produto produto) {
        carregarProdutos();
        produto.setIdProduto(verificarUltimoId(produtos) + 1);
        produtos.add(produto);
        ImportDal.gravarProdutos(produtos);
    }

    public static List<Produto> obterTodosProdutos() {
        return ImportDal.carregarProdutos();
    }

    public static void listarProdutos() {
        List<Produto> produtos = obterTodosProdutos();
        for (Produto produto : produtos) {
            System.out.println("ID: " + produto.getIdProduto() + " - Nome: " + produto.getNome() + " - Descrição: " + produto.getDescricao());
        }
    }

    public static Produto procurarProduto(int id) {
        obterTodosProdutos();
        for (Produto produto : produtos) {
            if (produto.getIdProduto() == id) {
                return produto;
            }
        }
        return null;

    }

    private static int verificarUltimoId(List<Produto> produtos) {
        int ultimoId = 0;

        // Percorrer a lista e encontrar o maior ID
        for (Produto produto : produtos) {
            // Verificar se o produto tem um ID válido
            if (produto.getIdProduto() > ultimoId) {
                ultimoId = produto.getIdProduto();
            }
        }

        return ultimoId;
    }


    public static void editarProduto(Produto produto) {
        System.out.println("Produto encontrado: " + produto);

        System.out.print("Novo nome (deixe vazio para manter): ");
        String novoNome = Tools.scanner.nextLine().trim();
        if (!novoNome.isEmpty()) {
            produto.setNome(novoNome);
        }

        System.out.print("Nova descrição (deixe vazio para manter): ");
        String novaDescricao = Tools.scanner.nextLine().trim();
        if (!novaDescricao.isEmpty()) {
            produto.setDescricao(novaDescricao);
        }

        List<Produto> produtosAtualizados = ProdutoBLL.obterTodosProdutos();

        for (int i = 0; i < produtosAtualizados.size(); i++) {
            if (produtosAtualizados.get(i).getIdProduto() == produto.getIdProduto()) {
                produtosAtualizados.set(i, produto);
                break;
            }
        }

        ImportDal.salvarProdutos(produtosAtualizados);

        System.out.println("Produto atualizado com sucesso!");
    }

    public static void eliminarProduto(Produto produto) {
        List<Produto> produtos = obterTodosProdutos();
        boolean produtoRemovido = false;

        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getIdProduto() == produto.getIdProduto()) {
                produtos.remove(i);
                produtoRemovido = true;
                break;
            }
        }

        if (produtoRemovido) {
            ImportDal.salvarProdutos(produtos);
            System.out.println("Produto eliminado com sucesso!");
        } else {
            System.out.println("[ERRO] Falha ao eliminar o produto.");
        }
    }
}
