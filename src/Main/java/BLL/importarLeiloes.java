package BLL;

import Controller.NegociacaoController;
import Model.*;
import Utils.Constantes;
import jakarta.mail.MessagingException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static Utils.Constantes.caminhosFicheiros.CSV_FILE_IMPORT_LEILOES;

public class importarLeiloes {

    public void importarLeiloes() {
        List<String> erros = new ArrayList<>();
        ResultadoOperacao resultadoOperacao = new ResultadoOperacao();

        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        ProdutoBLL produtoBLL = new ProdutoBLL();
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        LanceBLL lanceBLL = new LanceBLL();
        NegociacaoController negociacaoController = new NegociacaoController();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_IMPORT_LEILOES))) {

            String linha;

            // Assumindo que o CSV tem um cabeçalho
            br.readLine(); // descarta o cabeçalho

            while ((linha = br.readLine()) != null) {

                String[] campos = linha.split(";");

                String nomeProduto = campos[0].trim();
                String descricaoProduto = campos[1].trim();
                int idTipoLeilao = Integer.parseInt(campos[2].trim());
                LocalDate dataInicio = parseDate(campos[3].trim());
                LocalDate dataFim = parseDate(campos[4].trim());
                double valorFinal = Double.parseDouble(campos[5].trim());
                String nomeCliente = campos[6].trim();

                if (idTipoLeilao >= 1 && idTipoLeilao <= 3) {

                    // Criar o Produto
                    Produto produto = new Produto(0,Constantes.estadosProduto.RESERVADO,nomeProduto,descricaoProduto);
                    produtoBLL.adicionarProduto(produto);

                    // Verificar se o usuário existe
                    Utilizador cliente = utilizadorBLL.procurarUtilizadorByNome(nomeCliente);
                    if (cliente == null) {
                        cliente = utilizadorBLL.criarCliente(nomeCliente,null,null,null,null);
                    }
                    // Criar o Leilão
                    Leilao leilao = new Leilao(0,produto.getIdProduto(),null,idTipoLeilao,dataInicio.atStartOfDay(),dataFim.atStartOfDay(),null,null,null,Constantes.estadosLeilao.FECHADO);
                    leilaoBLL.adicionarLeilao(leilao);

                    switch (idTipoLeilao) {
                        case Constantes.tiposLeilao.ELETRONICO:
                            resultadoOperacao = lanceBLL.adicionarLanceEletronico(leilao.getId(),valorFinal,cliente.getId(),idTipoLeilao);
                            break;
                        case Constantes.tiposLeilao.CARTA_FECHADA:
                            resultadoOperacao = lanceBLL.adicionarLanceCartaFechada(leilao.getId(),valorFinal,cliente.getId(),idTipoLeilao);
                            break;
                        case Constantes.tiposLeilao.VENDA_DIRETA:
                            resultadoOperacao = lanceBLL.adicionarLanceDireto(leilao.getId(),valorFinal,cliente.getId(),idTipoLeilao);
                            break;
                        case Constantes.tiposLeilao.NEGOCIACAO:
                            break;
                        default:
                            break;
                    }

                } else if (idTipoLeilao == 4) {

                    Utilizador cliente = utilizadorBLL.procurarUtilizadorByNome(nomeCliente);
                    if (cliente == null) {
                        cliente = utilizadorBLL.criarCliente(nomeCliente,null,null,null,null);
                    }
                    resultadoOperacao = negociacaoController.criarNegociacao(cliente.getId(),nomeProduto,descricaoProduto,valorFinal);

                    // Primeiro converte o objeto para o tipo da negociacao
                    Negociacao negociacao = (Negociacao) resultadoOperacao.Objeto;
                    int idNegociacao = negociacao.getIdNegociacao();

                    lanceBLL.fazerProposta(idNegociacao,cliente.getId(),valorFinal);
                } else {
                    erros.add("Tipo de leilão desconhecido na linha: " + linha);
                }

            }

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        if (erros.isEmpty()) {
            System.out.println("Importação de leilões concluída com sucesso!");
        } else {
            System.out.println("Importado com algumas falhas:");
            for (String erro : erros) {
                System.out.println(erro);
            }
        }
    }

    private LocalDate parseDate(String dataTexto) {
        try {
            return LocalDate.parse(dataTexto, java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        } catch (Exception e) {
            return null;
        }
    }
}
