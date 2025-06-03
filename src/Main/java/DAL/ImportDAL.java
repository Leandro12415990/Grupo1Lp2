package DAL;

import Utils.Tools;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImportDAL {
    private static final Logger logger = Logger.getLogger(ImportDAL.class.getName());

    public <T> List<T> carregarRegistos(String caminhoFicheiro, int minimoCampos, Function<String[], T> conversor) {
        List<T> lista = new ArrayList<>();

        String linha = null;
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoFicheiro))) {
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                if (linha.trim().isEmpty()) {
                    continue; // Ignora linhas em branco
                }

                String[] dados = linha.split(Tools.separador(), -1);

                if (dados.length < minimoCampos) {
                    logger.log(Level.WARNING, "Linha inválida no CSV: {0}", linha);
                    continue;
                }

                try {
                    T item = conversor.apply(dados);
                    lista.add(item);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Erro ao processar linha do CSV: " + linha, e);
                }
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Linha inválida no CSV" + caminhoFicheiro + linha);
        }

        return lista;
    }


    public <T> void gravarRegistos(String caminhoFicheiro, String cabecalho, List<T> lista, Function<T, String> conversor) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoFicheiro))) {
            bw.write(cabecalho);
            bw.newLine();

            // Cópia defensiva para evitar ConcurrentModificationException
            List<T> copiaLista = new ArrayList<>(lista);

            for (T item : copiaLista) {
                bw.write(conversor.apply(item));
                bw.newLine();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao gravar o ficheiro CSV: " + caminhoFicheiro, e);
        }
    }

    public List<String[]> lerLinhasCSV(String caminhoFicheiro, int minimoCampos) {
        List<String[]> linhas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoFicheiro))) {
            boolean primeiraLinha = true;
            String linha;

            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                if (linha.trim().isEmpty()) {
                    continue;
                }

                String[] dados = linha.split(Tools.separador(), -1);

                if (dados.length < minimoCampos) {
                    logger.log(Level.WARNING, "Linha inválida no CSV (campos insuficientes): {0}", linha);
                    continue;
                }

                linhas.add(dados);
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao ler o ficheiro CSV: " + caminhoFicheiro, e);
        }

        return linhas;
    }


}