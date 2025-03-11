package DAL;

import Model.Lance;
import Model.Leilao;
import Model.Utilizador;
import Utils.Tools;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ImportDal {
    private static final String CSV_FILE = "data\\Leilao.csv";
    private static final String CSV_FILE_UTILIZADOR = "data\\Utilizador.csv";
    private static final String CSV_FILE_LANCE = "data\\Lance.csv";

    public static List<Lance> carregarLance() {
        List<Lance> lances = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(CSV_FILE_LANCE))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                String[] dados = linha.split(Tools.separador(), -1);

                if (dados.length < 9) {
                    System.err.println("Linha inválida no CSV: " + linha);
                    continue;
                }

                try {
                    int idLance = Integer.parseInt(dados[0]);
                    int idLeilao = Integer.parseInt(dados[1]);
                    int idCliente = Integer.parseInt(dados[2]);
                    String nomeCliente = dados[3];
                    String emailCliente = dados[4];
                    double valorLance = Double.parseDouble(dados[5]);
                    int numLance = dados[6].isEmpty() ? 0 : Integer.parseInt(dados[6]);
                    int pontosUtilizados = dados[7].isEmpty() ? 0 : Integer.parseInt(dados[7]);
                    LocalDateTime dataLance = LocalDateTime.parse(dados[8], Tools.DATA_HORA);
                    int ordemLance = (dados.length > 9 && !dados[9].isEmpty()) ? Integer.parseInt(dados[9]) : 0;

                    Lance lance = new Lance(idLance, idLeilao, idCliente, nomeCliente, emailCliente,
                            valorLance, numLance, pontosUtilizados, dataLance, ordemLance);
                    lances.add(lance);
                } catch (NumberFormatException e) {
                    System.err.println("Erro ao converter valores numéricos: " + linha);
                } catch (Exception e) {
                    System.err.println("Erro ao processar linha do CSV: " + linha);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o ficheiro CSV: " + e.getMessage());
        }
        return lances;
    }

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
                int tipoUtilizador = Integer.parseInt(dados[8]);

                Utilizador utilizador = new Utilizador(id, nomeUtilizador, email, dataNascimento, morada, password, dataRegisto, ultimoLogin, tipoUtilizador);
                Tools.utilizadores.add(utilizador);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o ficheiro CSV: " + e.getMessage());
        }
        return Tools.utilizadores;
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

    public static void gravarLance(List<Lance> lances) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_LANCE))) {
            bw.write("ID APOSTA;ID LEILÃO;ID CLIENTE;NOME CLIENTE;EMAIL;VALOR APOSTA;MULTIPLOS UTILIZADOS;PONTOS UTILIZADOS;DATA APOSTA;ORDEM DA APOSTA");
            bw.newLine();

            for (Lance lance : lances) {
                String dataAposta = Tools.formatDateTime(lance.getDataLance());

                bw.write(lance.getIdLance() + Tools.separador() +
                        lance.getIdLeilao() + Tools.separador() +
                        lance.getIdCliente() + Tools.separador() +
                        lance.getNomeCliente() + Tools.separador() +
                        lance.getEmailCliente() + Tools.separador() +
                        lance.getValorLance() + Tools.separador() +
                        lance.getNumLance() + Tools.separador() +
                        lance.getValorLanceEletronio() + Tools.separador() +
                        dataAposta + Tools.separador() +
                        lance.getOrdemLance());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao gravar o ficheiro CSV Lances: " + e.getMessage());
        }
    }

}
