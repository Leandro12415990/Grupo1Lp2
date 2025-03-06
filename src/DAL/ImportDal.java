package DAL;

import Model.Leilao;
import Model.Utilizador;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ImportDal {
    private static final String CSV_FILE = "data\\Leilao.csv";
    private static final String CSV_FILE_UTILIZADOR = "data\\Utilizador.csv";

    public static List<Leilao> carregarLeilao() {
        List<Leilao> leiloes = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] dados = linha.split(";", -1);

                if (dados.length < 8) {
                    System.err.println("Linha inválida no CSV: " + linha);
                    continue;
                }

                int id = Integer.parseInt(dados[0]);
                String nomeProduto = dados[1];
                String descricao = dados[2];
                String tipoLeilao = dados[3];
                LocalDate dataInicio = LocalDate.parse(dados[4], formatter);
                LocalDate dataFim = dados[5].isEmpty() ? null : LocalDate.parse(dados[5], formatter);
                Double valorMinimo = Double.parseDouble(dados[6]);
                Double valorMaximo = dados[7].isEmpty() ? null : Double.parseDouble(dados[7]);
                Double multiploLance = (dados.length > 8 && !dados[8].isEmpty()) ? Double.parseDouble(dados[8]) : null;

                Leilao leilao = new Leilao(id, nomeProduto, descricao, tipoLeilao, dataInicio, dataFim,
                        valorMinimo, valorMaximo, multiploLance);
                leiloes.add(leilao);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o ficheiro CSV: " + e.getMessage());
        }
        return leiloes;
    }

    public static List<Utilizador> carregarUtilizador() {
        List<Utilizador> utilizadores = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_UTILIZADOR))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] dados = linha.split(";", -1);

                if (dados.length < 9) {
                    System.err.println("Linha inválida no CSV: " + linha);
                    continue;
                }

                int id = Integer.parseInt(dados[0]);
                String nomeUtilizador = dados[1];
                String email = dados[2];
                LocalDate dataNascimento = LocalDate.parse(dados[3], formatter);
                String morada = dados[4];
                String password = dados[5];
                LocalDate dataRegisto = LocalDate.parse(dados[6], formatter);
                LocalDate ultimoLogin = dados[7].isEmpty() ? null : LocalDate.parse(dados[7], formatter);
                String tipoUtilizador = dados[8];

                Utilizador utilizador = new Utilizador(id, nomeUtilizador, email, dataNascimento, morada, password, dataRegisto, ultimoLogin, tipoUtilizador);
                utilizadores.add(utilizador);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o ficheiro CSV: " + e.getMessage());
        }
        return utilizadores;
    }

    public static void gravarLeilao(List<Leilao> leiloes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            bw.write("ID;PRODUTO;DESCRICAO;TIPO LEILAO;DATA INICIO;DATA FIM;VALOR MINIMO;VALOR MAXIMO;MULTIPLO BID");
            bw.newLine();

            for (Leilao leilao : leiloes) {
                String dataFim = leilao.getDataFim() != null ? leilao.getDataFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
                String valorMaximo = leilao.getValorMaximo() != null ? leilao.getValorMaximo().toString() : "";
                String multiploLance = leilao.getMultiploLance() != null ? leilao.getMultiploLance().toString() : "";

                bw.write(leilao.getId() + ";" +
                        leilao.getNomeProduto() + ";" +
                        leilao.getDescricao() + ";" +
                        leilao.getTipoLeilao() + ";" +
                        leilao.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ";" +
                        dataFim + ";" +
                        leilao.getValorMinimo() + ";" +
                        valorMaximo + ";" +
                        multiploLance);
                bw.newLine();
            }

        } catch (IOException e) {
            System.err.println("Erro ao gravar o ficheiro CSV de Leilões: " + e.getMessage());
        }
    }

    public static void gravarUtilizador(List<Utilizador> utilizadores) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_UTILIZADOR))) {
            bw.write("ID;NOME;EMAIL;DATA NASCIMENTO;MORADA;PASSWORD;DATA REGISTO;ULTIMO LOGIN;TIPO UTILIZADOR");
            bw.newLine();

            for (Utilizador utilizador : utilizadores) {
                String ultimoLogin = utilizador.getUltimoLogin() != null ? utilizador.getUltimoLogin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";

                bw.write(utilizador.getId() + ";" +
                        utilizador.getNomeUtilizador() + ";" +
                        utilizador.getEmail() + ";" +
                        utilizador.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ";" +
                        utilizador.getMorada() + ";" +
                        utilizador.getPassword() + ";" +
                        utilizador.getDataRegisto().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ";" +
                        ultimoLogin + ";" +
                        utilizador.getTipoUtilizador());
                bw.newLine();
            }

        } catch (IOException e) {
            System.err.println("Erro ao gravar o ficheiro CSV Utilizadores: " + e.getMessage());
        }
    }
}
