package BLL;

import DAL.ImportDal;
import Model.Produto;

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
        try (BufferedReader br = new BufferedReader(new FileReader("produtos.csv"))) {
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
            System.out.println("Nome: " + produto.getNome() + " - Descrição: " + produto.getDescricao());
        }
    }

}
