package DAL;

import Model.Lance;
import Model.Leilao;
import Model.Transacao;
import Model.Utilizador;
import Utils.Tools;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImportDal {
    private static final String CSV_FILE = "data\\Leilao.csv";
    private static final String CSV_FILE_UTILIZADOR = "data\\Utilizador.csv";
    private static final String CSV_FILE_LANCE = "data\\Lance.csv";
    private static final String CSV_FILE_TRANSACAO = "data\\Transacao.csv";

    private static final Logger logger = Logger.getLogger(ImportDal.class.getName());

    public List<Lance> carregarLance() {
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

                if (dados.length < 7) {
                    logger.log(Level.WARNING, "Linha inválida no CSV de Lances: {0}", linha);
                    continue;
                }

                try {
                    int idLance = Integer.parseInt(dados[0]);
                    int idLeilao = Integer.parseInt(dados[1]);
                    int idCliente = Integer.parseInt(dados[2]);
                    double valorLance = Double.parseDouble(dados[3]);
                    int numLance = dados[4].isEmpty() ? 0 : Integer.parseInt(dados[4]);
                    int pontosUtilizados = dados[5].isEmpty() ? 0 : Integer.parseInt(dados[5]);
                    LocalDateTime dataLance = Tools.parseDateTimeByDate(dados[6]);

                    Lance lance = new Lance(idLance, idLeilao, idCliente,
                            valorLance, numLance, pontosUtilizados, dataLance);
                    lances.add(lance);
                } catch (NumberFormatException e) {
                    logger.log(Level.WARNING, "Erro ao converter valores numéricos: {0}", linha);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Erro ao processar linha do CSV: " + linha, e);
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao ler o ficheiro CSV de Lances", e);
        }
        return lances;
    }

    public List<Leilao> carregarLeilao() {
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
                    logger.log(Level.WARNING, "Linha inválida no CSV: {0}", linha);
                    continue;
                }

                int id = Integer.parseInt(dados[0]);
                int idProduto = Integer.parseInt(dados[1]);
                String descricao = dados[2];
                int idTipoLeilao = Integer.parseInt(dados[3]);
                LocalDateTime dataInicio = Tools.parseDateTimeByDate(dados[4]);
                LocalDateTime dataFim = dados[5].isEmpty() ? null : Tools.parseDateTimeByDate(dados[5]);
                Double valorMinimo = Double.parseDouble(dados[6]);
                Double valorMaximo = dados[7].isEmpty() ? null : Double.parseDouble(dados[7]);
                Double multiploLance = (dados.length > 8 && !dados[8].isEmpty()) ? Double.parseDouble(dados[8]) : null;
                int idEstado = Integer.parseInt(dados[9]);

                Leilao leilao = new Leilao(id, idProduto, descricao, idTipoLeilao, dataInicio, dataFim,
                        valorMinimo, valorMaximo, multiploLance, idEstado);
                leiloes.add(leilao);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao ler o ficheiro CSV de Leilões", e);
        }
        return leiloes;
    }

    public List<Utilizador> carregarUtilizador() {
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
                    logger.log(Level.WARNING, "Linha inválida no CSV de Utilizadores: {0}", linha);
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
                int estado = Integer.parseInt(dados[9]);
                Double saldo = Double.parseDouble(dados[10]);

                Utilizador utilizador = new Utilizador(id, nomeUtilizador, email, dataNascimento, morada, password, dataRegisto, ultimoLogin, tipoUtilizador, estado, saldo);
                utilizadores.add(utilizador);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao ler o ficheiro CSV de Utilizadores", e);
        }
        return utilizadores;
    }

    public List<Transacao> carregarTransacao() {
        List<Transacao> transacaoList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_TRANSACAO))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }
                String[] dados = linha.split(Tools.separador(), -1);

                if (dados.length < 6) {
                    logger.log(Level.WARNING, "Linha inválida no CSV de Transações: {0}", linha);
                    continue;
                }

                int idTransacao = Integer.parseInt(dados[0]);
                int idCliente = Integer.parseInt(dados[1]);
                Double valorTotal = Double.parseDouble(dados[2]);
                Double valorTransacao = Double.parseDouble(dados[3]);
                LocalDateTime dataTransacao = Tools.parseDateTimeByDate(dados[4]);
                int idTipoTransacao = Integer.parseInt(dados[5]);
                int idEstadoTransacao = Integer.parseInt(dados[6]);

                Transacao transacao = new Transacao(idTransacao, idCliente, valorTotal, valorTransacao, dataTransacao, idTipoTransacao, idEstadoTransacao);
                transacaoList.add(transacao);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao ler o ficheiro CSV de Transações", e);
        }
        return transacaoList;
    }

    public void gravarLeilao(List<Leilao> leiloes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE))) {
            bw.write("ID;ID_PRODUTO;DESCRICAO;ID_TIPO_LEILAO;DATA_INICIO;DATA_FIM;VALOR_MINIMO;VALOR_MAXIMO;MULTIPLO_LANCE;ID_ESTADO");
            bw.newLine();

            for (Leilao leilao : leiloes) {
                String dataInicio = Tools.formatDateTime(leilao.getDataInicio());
                String dataFim = Tools.formatDateTime(leilao.getDataFim());
                String valorMaximo = leilao.getValorMaximo() != null ? leilao.getValorMaximo().toString() : "";
                String multiploLance = leilao.getMultiploLance() != null ? leilao.getMultiploLance().toString() : "";

                bw.write(leilao.getId() + Tools.separador() +
                        leilao.getIdProduto() + Tools.separador() +
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
            logger.log(Level.SEVERE, "Erro ao gravar o ficheiro CSV de Leilões", e);
        }
    }

    public void gravarUtilizador(List<Utilizador> utilizadores) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_UTILIZADOR))) {
            bw.write("ID;NOME;EMAIL;DATA NASCIMENTO;MORADA;PASSWORD;DATA REGISTO;ULTIMO LOGIN;TIPO UTILIZADOR;ESTADO;SALDO");
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
                        utilizador.getEstado() + Tools.separador() +
                        utilizador.getSaldo());
                bw.newLine();
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao gravar o ficheiro CSV de Utilizadores", e);
        }
    }

    public void gravarLance(List<Lance> lances) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_LANCE))) {
            bw.write("ID APOSTA;ID LEILÃO;ID CLIENTE;VALOR APOSTA;MULTIPLOS UTILIZADOS;PONTOS UTILIZADOS;DATA APOSTA");
            bw.newLine();

            for (Lance lance : lances) {
                String dataAposta = Tools.formatDateTime(lance.getDataLance());

                bw.write(lance.getIdLance() + Tools.separador() +
                        lance.getIdLeilao() + Tools.separador() +
                        lance.getIdCliente() + Tools.separador() +
                        lance.getValorLance() + Tools.separador() +
                        lance.getNumLance() + Tools.separador() +
                        lance.getPontosUtilizados() + Tools.separador() +
                        dataAposta + Tools.separador());
                bw.newLine();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao gravar o ficheiro CSV de Lances", e);
        }
    }

    public void gravarTransacao(List<Transacao> transacaoList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_TRANSACAO))) {
            bw.write("ID_TRANSACAO;ID_CLIENTE;VALOR_TOTAL;VALOR_TRANSACAO;DATA_TRANSACAO;ID_TIPO;ID_ESTADO");
            bw.newLine();

            for (Transacao transacao : transacaoList) {
                String dataTransacao = Tools.formatDateTime(transacao.getDataTransacao());

                bw.write(transacao.getIdTransacao() + Tools.separador() +
                        transacao.getIdCliente() + Tools.separador() +
                        transacao.getValorTotal() + Tools.separador() +
                        transacao.getValorTransacao() + Tools.separador() +
                        dataTransacao + Tools.separador() +
                        transacao.getIdTipoTransacao() + Tools.separador() +
                        transacao.getIdEstadoTransacao());
                bw.newLine();
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao gravar o ficheiro CSV de Transações", e);
        }
    }


}
