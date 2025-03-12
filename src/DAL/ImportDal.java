package DAL;

import Model.Leilao;
import Model.Utilizador;
import Utils.Tools;

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

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] dados = linha.split(Tools.separador(), -1);

                if (dados.length < 8) {
                    System.err.println("Linha inválida no CSV: " + linha);
                    continue;
                }

                int id = Integer.parseInt(dados[0]);
                String nomeProduto = dados[1];
                String descricao = dados[2];
                String tipoLeilao = dados[3];
                LocalDate dataInicio = Tools.parseDate(dados[4]);
                LocalDate dataFim = dados[5].isEmpty() ? null : Tools.parseDate(dados[5]);
                Double valorMinimo = Double.parseDouble(dados[6]);
                Double valorMaximo = dados[7].isEmpty() ? null : Double.parseDouble(dados[7]);
                Double multiploLance = (dados.length > 8 && !dados[8].isEmpty()) ? Double.parseDouble(dados[8]) : null;
                String estado = dados[9];

                Leilao leilao = new Leilao(id, nomeProduto, descricao, tipoLeilao, dataInicio, dataFim,
                        valorMinimo, valorMaximo, multiploLance, estado);
                leiloes.add(leilao);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o ficheiro CSV: " + e.getMessage());
        }
        return leiloes;
    }

    public static List<Utilizador> carregarUtilizador() {
        List<Utilizador> utilizadores = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_UTILIZADOR))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] dados = linha.split(Tools.separador(), -1);

                if (dados.length < 10) {
                    System.err.println("Linha inválida no CSV: " + linha);
                    continue;
                }

                int id = Integer.parseInt(dados[0]);
                String nomeUtilizador = dados[1];
                String email = dados[2];
                LocalDate dataNascimento = Tools.parseDate(dados[3]);
                String morada = dados[4];
                String password = dados[5];
                LocalDate dataRegisto = Tools.parseDate(dados[6]);
                LocalDate ultimoLogin = dados[7].isEmpty() ? null : Tools.parseDate(dados[7]);
                int tipoUtilizador = Integer.parseInt(dados[8]);
                String estado = dados[9];

                Utilizador utilizador = new Utilizador(id, nomeUtilizador, email, dataNascimento, morada, password, dataRegisto, ultimoLogin, tipoUtilizador, estado);
                utilizadores.add(utilizador);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o ficheiro CSV: " + e.getMessage());
        }
        return utilizadores;
    }

    public static void gravarLeilao(List<Leilao> leiloes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            bw.write("ID;PRODUTO;DESCRICAO;TIPO LEILAO;DATA INICIO;DATA FIM;VALOR MINIMO;VALOR MAXIMO;MULTIPLO BID;ESTADO");
            bw.newLine();

            for (Leilao leilao : leiloes) {
                String dataInicio = Tools.formatDate(leilao.getDataInicio());
                String dataFim = Tools.formatDate(leilao.getDataFim());
                String valorMaximo = leilao.getValorMaximo() != null ? leilao.getValorMaximo().toString() : "";
                String multiploLance = leilao.getMultiploLance() != null ? leilao.getMultiploLance().toString() : "";

                bw.write(leilao.getId() + Tools.separador() +
                        leilao.getNomeProduto() + Tools.separador() +
                        leilao.getDescricao() + Tools.separador() +
                        leilao.getTipoLeilao() + Tools.separador() +
                        dataInicio + Tools.separador() +
                        dataFim + Tools.separador() +
                        leilao.getValorMinimo() + Tools.separador() +
                        valorMaximo + Tools.separador() +
                        multiploLance + Tools.separador() +
                        leilao.getEstado());
                bw.newLine();
            }

        } catch (IOException e) {
            System.err.println("Erro ao gravar o ficheiro CSV de Leilões: " + e.getMessage());
        }
    }

    public static void gravarUtilizador(List<Utilizador> utilizadores) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_UTILIZADOR))) {
            bw.write("ID;NOME;EMAIL;DATA NASCIMENTO;MORADA;PASSWORD;DATA REGISTO;ULTIMO LOGIN;TIPO UTILIZADOR;ESTADO");
            bw.newLine();

            for (Utilizador utilizador : utilizadores) {
                String dataNascimento = Tools.formatDate(utilizador.getDataNascimento());
                String dataRegisto = Tools.formatDate(utilizador.getDataRegisto());
                String ultimoLogin = Tools.formatDate(utilizador.getUltimoLogin());

                bw.write(utilizador.getId() + Tools.separador() +
                        utilizador.getNomeUtilizador() + Tools.separador() +
                        utilizador.getEmail() + Tools.separador() +
                        dataNascimento + Tools.separador() +
                        utilizador.getMorada() + Tools.separador() +
                        utilizador.getPassword() + Tools.separador() +
                        dataRegisto + Tools.separador() +
                        ultimoLogin + Tools.separador() +
                        utilizador.getTipoUtilizador() + Tools.separador() +
                        utilizador.getEstado());
                bw.newLine();
            }

        } catch (IOException e) {
            System.err.println("Erro ao gravar o ficheiro CSV Utilizadores: " + e.getMessage());
        }
    }

}
