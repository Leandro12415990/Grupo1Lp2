package DAL;

import Model.Produto;
import Utils.Tools;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProdutoDal {
    private static final String CSV_FILE_PRODUTO = "data\\Produto.csv";

    private static final Logger logger = Logger.getLogger(ProdutoDal.class.getName());

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

                if (dados.length < 4) {
                    logger.log(Level.WARNING, "[ERRO] Linha inválida no CSV: ", linha);
                    continue;
                }

                try {
                    int id = Integer.parseInt(dados[0].trim());
                    int estado = Integer.parseInt(dados[1].trim());
                    String nome = dados[2].trim();
                    String descricao = dados[3].trim();

                    Produto produto = new Produto(id, estado, nome, descricao);
                    produtos.add(produto);

                } catch (NumberFormatException e) {
                    logger.log(Level.WARNING,"[ERRO] ID inválido no CSV: ", dados[0]);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE,"[ERRO] Problema ao ler o ficheiro CSV: ", e.getMessage());
        }

        return produtos;
    }

    public static void gravarProdutos(List<Produto> produtos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PRODUTO))) {
            bw.write("IDPRODUTO;ESTADO;NOME;DESCRICAO");
            bw.newLine();

            for (Produto produto : produtos) {
                bw.write(produto.getIdProduto() + Tools.separador() +
                        produto.getEstado() + Tools.separador() +
                        produto.getNome() + Tools.separador() +
                        produto.getDescricao() + Tools.separador());
                bw.newLine();
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE,"Erro ao gravar o ficheiro CSV de Produtos: ", e.getMessage());
        }
    }

}
