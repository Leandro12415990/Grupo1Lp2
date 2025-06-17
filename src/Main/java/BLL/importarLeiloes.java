package BLL;

import Controller.NegociacaoController;
import DAL.LanceDAL;
import DAL.LeilaoDAL;
import Model.*;
import Utils.Constantes;
import Utils.Tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static Utils.Constantes.caminhosFicheiros.CSV_FILE_IMPORT_LEILOES;

public class importarLeiloes {

    public ResultadoImportacao importarLeiloes() {
        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        ProdutoBLL produtoBLL = new ProdutoBLL();
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        LeilaoDAL leilaoDAL = new LeilaoDAL();
        LanceBLL lanceBLL = new LanceBLL();
        LanceDAL lanceDAL = new LanceDAL();
        NegociacaoController negociacaoController = new NegociacaoController();


        int totalImportados = 0;
        List<String> erros = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_IMPORT_LEILOES))) {

            String linha;
            br.readLine();

            List<Leilao> listaLeiloes = leilaoDAL.carregaLeiloes();
            List<Lance> listaLances = lanceBLL.carregarLances();

            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(Tools.separador());
                if (campos.length < 7) {
                    erros.add("Linha incompleta: " + linha);
                    continue;
                }

                String nomeProduto = campos[0].trim();
                String descricaoProduto = campos[1].trim();
                int idTipoLeilao = Integer.parseInt(campos[2].trim());
                LocalDate dataInicio = parseDate(campos[3].trim());
                LocalDate dataFim = parseDate(campos[4].trim());
                double valorFinal = Double.parseDouble(campos[5].trim());
                String nomeCliente = campos[6].trim();

                try {
                    Utilizador cliente = utilizadorBLL.procurarUtilizadorByNome(nomeCliente);
                    if (cliente == null) {
                        String emailFake = nomeCliente.toLowerCase().replace(" ", ".") + "@email.com";
                        cliente = utilizadorBLL.criarCliente(nomeCliente, emailFake, null, null, null);
                    }

                    if (idTipoLeilao >= 1 && idTipoLeilao <= 3) {
                        Produto produto = new Produto(
                                0,
                                Constantes.estadosProduto.RESERVADO,
                                nomeProduto,
                                descricaoProduto
                        );
                        produtoBLL.adicionarProduto(produto);

                        int novoIdLeilao = verificarUltimoId(listaLeiloes) + 1;
                        Leilao leilao = new Leilao(
                                novoIdLeilao,
                                produto.getIdProduto(),
                                null,
                                idTipoLeilao,
                                dataInicio.atStartOfDay(),
                                dataFim.atStartOfDay(),
                                0.0,
                                0.0,
                                1.0,
                                Constantes.estadosLeilao.FECHADO
                        );
                        listaLeiloes.add(leilao);

                        int novoIdLance = verificarUltimoIdLance(listaLances) + 1;
                        Lance lance = new Lance(
                                novoIdLance,
                                novoIdLeilao,
                                cliente.getId(),
                                valorFinal,
                                0,
                                0.0,
                                LocalDateTime.now(),
                                Constantes.estadosLance.DEFAULT
                        );
                        listaLances.add(lance);
                        totalImportados ++;

                    } else if (idTipoLeilao == Constantes.tiposLeilao.NEGOCIACAO) {
                        ResultadoOperacao resultado = negociacaoController.criarNegociacao(
                                cliente.getId(),
                                nomeProduto,
                                descricaoProduto,
                                valorFinal
                        );
                        Negociacao negociacao = (Negociacao) resultado.Objeto;

                        int novoIdLance = verificarUltimoIdLance(listaLances) + 1;
                        Lance lance = new Lance(
                                novoIdLance,
                                0,
                                cliente.getId(),
                                0.0,
                                negociacao.getIdNegociacao(),
                                valorFinal,
                                LocalDateTime.now(),
                                Constantes.estadosLance.FINALIZADO
                        );
                        listaLances.add(lance);
                        totalImportados ++;
                    } else {
                        erros.add("Tipo de leilão desconhecido: " + idTipoLeilao + " na linha: " + linha);
                    }

                } catch (Exception e) {
                    erros.add("Erro ao processar linha: " + linha + " -> " + e.getMessage());
                }
            }

            leilaoDAL.gravarLeiloes(listaLeiloes);
            lanceDAL.gravarLances(listaLances);

        } catch (IOException e) {
            erros.add("Erro ao ler o ficheiro: " + e.getMessage());
        }
        return new ResultadoImportacao(null, totalImportados, 0, erros);

    }

    private LocalDate parseDate(String dataTexto) {
        try {
            return LocalDate.parse(dataTexto, DateTimeFormatter.ofPattern("yyyyMMdd"));
        } catch (Exception e) {
            return null;
        }
    }

    private int verificarUltimoId(List<Leilao> lista) {
        int maior = 0;
        for (Leilao l : lista) {
            if (l.getId() > maior) {
                maior = l.getId();
            }
        }
        return maior;
    }

    private int verificarUltimoIdLance(List<Lance> lista) {
        int maior = 0;
        for (Lance l : lista) {
            if (l.getIdLance() > maior) {
                maior = l.getIdLance();
            }
        }
        return maior;
    }

}