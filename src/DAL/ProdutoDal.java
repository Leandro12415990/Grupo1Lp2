package DAL;

import Model.Produto;
import Utils.Tools;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ProdutoDal {
    private static final String CSV_FILE_PRODUTO = "data\\Produto.csv"; // Caminho do arquivo CSV

    public static List<Produto> carregarProdutos() {
        List<Produto> produtos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PRODUTO))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] dados = linha.split(Tools.separador(), -1);

                if (dados.length < 3) {
                    System.err.println("[ERRO] Linha inválida no CSV: " + linha);
                    continue;
                }

                try {
                    int id = Integer.parseInt(dados[0].trim());
                    String nome = dados[1].trim();
                    String descricao = dados[2].trim();

                    Produto produto = new Produto(id, nome, descricao);
                    produtos.add(produto);

                } catch (NumberFormatException e) {
                    System.err.println("[ERRO] ID inválido no CSV: " + dados[0]);
                }
            }
        } catch (IOException e) {
            System.err.println("[ERRO] Problema ao ler o ficheiro CSV: " + e.getMessage());
        }

        return produtos;
    }

    public static void gravarProdutos(List<Produto> produtos) {
        /*try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PRODUTO, true))) {
            bw.write(produto.getIdProduto() + ";" + produto.getNome() + ";" + produto.getDescricao());
            bw.newLine();
            System.out.println("Produto adicionado ao CSV: " + produto.getNome());
        } catch (IOException e) {
            System.err.println("Erro ao gravar o produto no CSV: " + e.getMessage());
        }*/

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PRODUTO))) {
            bw.write("IDPRODUTO;NOME;DESCRICAO");
            bw.newLine();

            for (Produto produto : produtos) {
                bw.write(produto.getIdProduto() + Tools.separador() +
                        produto.getNome() + Tools.separador() +
                        produto.getDescricao() + Tools.separador());
                bw.newLine();
            }

        } catch (IOException e) {
            System.err.println("Erro ao gravar o ficheiro CSV de Produtos: " + e.getMessage());
        }
    }

    public static void salvarProdutos(List<Produto> produtos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PRODUTO))) {
            bw.write("ID;Nome;Descrição");
            bw.newLine();

            for (Produto produto : produtos) {
                bw.write(produto.getIdProduto() + ";" + produto.getNome() + ";" + produto.getDescricao());
                bw.newLine();
            }

            System.out.println("Ficheiro atualizado com sucesso!");
        } catch (IOException e) {
            System.err.println("[ERRO] Não foi possível salvar o ficheiro: " + e.getMessage());
        }
    }


}
