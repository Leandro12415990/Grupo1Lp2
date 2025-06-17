package BLL;

import Controller.LeilaoController;
import Controller.NegociacaoController;
import DAL.LanceDAL;
import DAL.LeilaoDAL;
import Model.*;
import Utils.Constantes;
import jakarta.mail.MessagingException;

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

    public void importarLeiloes() {
        List<String> erros = new ArrayList<>();

        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        ProdutoBLL produtoBLL = new ProdutoBLL();
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        LeilaoDAL leilaoDAL = new LeilaoDAL();
        LanceBLL lanceBLL = new LanceBLL();
        LanceDAL lanceDAL = new LanceDAL();
        NegociacaoController negociacaoController = new NegociacaoController();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_IMPORT_LEILOES))) {

            String linha;
            br.readLine(); // Ignorar cabeçalho

            // ✅ Pré-carregar listas completas:
            List<Leilao> listaLeiloes = leilaoDAL.carregaLeiloes();
            List<Lance> listaLances = lanceBLL.carregarLances();

            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(";");
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
                    // ✅ Cliente (sempre)
                    Utilizador cliente = utilizadorBLL.procurarUtilizadorByNome(nomeCliente);
                    if (cliente == null) {
                        String emailFake = nomeCliente.toLowerCase().replace(" ", ".") + "@email.com";
                        cliente = utilizadorBLL.criarCliente(nomeCliente, emailFake, null, null, null);
                    }

                    if (idTipoLeilao >= 1 && idTipoLeilao <= 3) {
                        // ✅ Produto - só para leilões
                        Produto produto = new Produto(
                                0,
                                Constantes.estadosProduto.RESERVADO,
                                nomeProduto,
                                descricaoProduto
                        );
                        produtoBLL.adicionarProduto(produto);

                        // ✅ Leilão
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

                        // ✅ Lance
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

                    } else if (idTipoLeilao == Constantes.tiposLeilao.NEGOCIACAO) {
                        // ✅ Só cria Negociação + Lance de proposta — NÃO cria Produto nem Leilão
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
                                0, // idLeilao = 0
                                cliente.getId(),
                                0.0,
                                negociacao.getIdNegociacao(),
                                valorFinal,
                                LocalDateTime.now(),
                                Constantes.estadosLance.DEFAULT
                        );
                        listaLances.add(lance);

                    } else {
                        erros.add("Tipo de leilão desconhecido: " + idTipoLeilao + " na linha: " + linha);
                    }

                } catch (Exception e) {
                    erros.add("Erro ao processar linha: " + linha + " -> " + e.getMessage());
                }
            }

            // ✅ Gravar TUDO de uma só vez no fim!
            leilaoDAL.gravarLeiloes(listaLeiloes);
            lanceDAL.gravarLances(listaLances);

        } catch (IOException e) {
            erros.add("Erro ao ler o ficheiro: " + e.getMessage());
        }

        // ✅ Resultado final
        if (erros.isEmpty()) {
            System.out.println("✅ Importação concluída com sucesso!");
        } else {
            System.out.println("⚠️ Importação concluída com erros:");
            erros.forEach(System.out::println);
        }
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



/*package BLL;

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
*/