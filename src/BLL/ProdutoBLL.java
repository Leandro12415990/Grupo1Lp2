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
        produtos.add(produto);
        ImportDal.gravarProdutos(produto);
    }

    public static boolean produtoExiste(String nome) {
        try (BufferedReader br = new BufferedReader(new FileReader("data\\Produto.csv"))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] dados = linha.split(";", -1);
                if (dados.length >= 2 && dados[0].trim().equalsIgnoreCase(nome)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao verificar produto no CSV: " + e.getMessage());
        }

        return false;
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

    public static List<Produto> VerificarIdProdutos() {
        if (produtos == null || produtos.isEmpty()) {
            produtos = ImportDal.carregarProdutos();
        }

        int maiorId = 0;
        for (Produto produto : produtos) {
            if (produto.getIdProduto() > maiorId) {
                maiorId = produto.getIdProduto();
            }
        }

        Produto.atualizarContador(maiorId);
        return produtos;
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

        // 🔹 Percorrer a lista e remover o produto baseado no ID
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getIdProduto() == produto.getIdProduto()) {
                produtos.remove(i); // Remover pelo índice
                produtoRemovido = true;
                break;
            }
        }

        // 🔹 Se o produto foi removido, salvar a lista no ficheiro CSV
        if (produtoRemovido) {
            ImportDal.salvarProdutos(produtos);
            System.out.println("✅ Produto eliminado com sucesso!");
        } else {
            System.out.println("[ERRO] Falha ao eliminar o produto.");
        }
    }
}
