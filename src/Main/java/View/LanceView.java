package View;

import BLL.LanceBLL;
import Controller.*;
import DAL.UtilizadorDAL;
import Model.*;
import Utils.Constantes;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.List;

import static Utils.Tools.scanner;

public class LanceView {
    public void exibirMenuLance() throws MessagingException, IOException {
        int opc;
        do {
            System.out.println("\n" + "=".repeat(5) + " MENU LANCES " + "=".repeat(5));
            System.out.println("1. Ver os Meus Lances");
            System.out.println("2. Ver Leilões Terminados");
            System.out.println("3. Dar Lance Direto");
            System.out.println("4. Dar Lance Carta Fechada");
            System.out.println("5. Dar Lance Eletrónico");
            System.out.println("6. Leilões Particulares");
            System.out.println("0. Voltar ao menu principal...");
            opc = Tools.pedirOpcaoMenu("Escolha uma opção: ");

            switch (opc) {
                case 1 -> listarMeuLance();
                case 2 -> verDetalhesLeilaoTerminados();
                case 3 -> lanceDireto();
                case 4 -> lanceCartaFechada();
                case 5 -> lanceEletronico();
                case 6 -> verLeiloesDeOutrosEDarLance();
                case 0 -> System.out.println("\nSair...");
                default -> System.out.println("Opção inválida. Tente novamente...");
            }
        } while (opc != 0);
    }

    public void lanceDireto() throws MessagingException, IOException {
        UtilizadorDAL utilizadorDAL = new UtilizadorDAL();
        LanceController lanceController = new LanceController();
        LeilaoController leilaoController = new LeilaoController();

        ResultadoOperacao resultado;
        System.out.println("\n===== LEILÕES VENDA DIRETA =====");
        List<Leilao> leiloesAtivos = leilaoController.listarLeiloes(Tools.estadoLeilao.ATIVO);
        List<Utilizador> cliente = utilizadorDAL.carregarUtilizadores();
        List<Leilao> leiloesLanceDireto = lanceController.listarLeiloesByTipo(leiloesAtivos, Constantes.tiposLeilao.VENDA_DIRETA);
        if (!leiloesLanceDireto.isEmpty()) {
            for (Leilao leilao : leiloesLanceDireto) {
                System.out.println("ID: " + leilao.getId() + " | Produto: " + leilao.getDescricao() + " | Valor Lance: " + leilao.getValorMinimo());
            }

            int idLeilao = Tools.pedirInt("\nInsira o ID do leilão em que deseja participar " + Tools.alertaCancelar());
            if (Tools.verificarSaida(String.valueOf(idLeilao))) return;

            boolean verificarID = lanceController.verificarDisponibilidadeLeilao(leiloesLanceDireto, idLeilao);
            Leilao leilao = leilaoController.procurarLeilaoPorId(idLeilao);
            int idCliente = Tools.clienteSessao.getIdCliente();
            UtilizadorController utilizadorController = new UtilizadorController();
            double saldo = utilizadorController.obterSaldoCliente(idCliente);

            if (verificarID) {
                System.out.println("O seu saldo atual: " + saldo);
                System.out.print("Tem a certeza que quer dar um Lance? (S/N)" + Tools.alertaCancelar());
                String imput1 = scanner.nextLine().trim();
                if (Tools.verificarSaida(imput1)) return;
                char opc = Character.toUpperCase(imput1.charAt(0));
                if (opc == 'S') {
                    Double valorLance = leilao.getValorMinimo();
                    resultado = lanceController.adicionarLanceDireto(idLeilao, valorLance);
                    if (resultado.Sucesso) {
                        System.out.println("PARABÉNS! É O VENCEDOR!");
                    } else {
                        System.out.println("Créditos Insuficientes " + resultado.msgErro);
                    }
                } else if (opc == 'N') {
                    return;
                } else {
                    System.out.println("Opção inválida!");
                }
            } else {
                System.out.println("Leilão não disponível!");
            }
        } else {
            System.out.println("Não existem leilões disponíveis do tipo Venda Direta.");
        }
    }

    public void lanceCartaFechada() throws MessagingException, IOException {
        LanceController lanceController = new LanceController();
        LeilaoController leilaoController = new LeilaoController();
        System.out.println("\n===== LEILÕES CARTA FECHADA =====");

        List<Leilao> leiloesAtivos = leilaoController.listarLeiloes(Tools.estadoLeilao.ATIVO);
        List<Leilao> leilaoCartaFechada = lanceController.listarLeiloesByTipo(leiloesAtivos, Constantes.tiposLeilao.CARTA_FECHADA);

        if (!leilaoCartaFechada.isEmpty()) {
            for (Leilao leilao : leilaoCartaFechada) {
                System.out.println("ID: " + leilao.getId() + " | Produto: " + leilao.getDescricao());
            }

            int idLeilao = Tools.pedirInt("\nInsira o ID do leilão em que deseja participar " + Tools.alertaCancelar());
            if (Tools.verificarSaida(String.valueOf(idLeilao))) return;

            boolean verificarID = lanceController.verificarDisponibilidadeLeilao(leilaoCartaFechada, idLeilao);
            int idCliente = Tools.clienteSessao.getIdCliente();
            UtilizadorController utilizadorController = new UtilizadorController();
            double saldo = utilizadorController.obterSaldoCliente(idCliente);

            if (verificarID) {
                System.out.println("O seu saldo atual: " + saldo);
                double valorLance = Tools.pedirDouble("Insira o valor do lance " + Tools.alertaCancelar());
                if (Tools.verificarSaida(String.valueOf(valorLance))) return;

                ResultadoOperacao resultado = lanceController.adicionarLanceCartaFechada(idLeilao, valorLance);

                if (resultado.Sucesso) {
                    System.out.println("O seu Lance foi aceite");
                } else {
                    System.out.println(resultado.msgErro);
                }
            } else {
                System.out.println("Leilão indisponível!");
            }
        } else {
            System.out.println("Não existem leilões disponíveis do tipo Carta Fechada.");
        }
    }

    public void lanceEletronico() throws MessagingException, IOException {
        LanceController lanceController = new LanceController();
        LeilaoController leilaoController = new LeilaoController();
        System.out.println("\n===== LEILÕES ELETRÔNICOS =====");

        List<Leilao> leiloesAtivos = leilaoController.listarLeiloes(Tools.estadoLeilao.ATIVO);
        List<Leilao> leilaoEletronico = lanceController.listarLeiloesByTipo(leiloesAtivos, Constantes.tiposLeilao.ELETRONICO);

        if (!leilaoEletronico.isEmpty()) {
            for (Leilao leilao : leilaoEletronico) {
                System.out.println("ID: " + leilao.getId() + " | Produto: " + leilao.getDescricao() +
                        " | Valor do Múltiplo de Lance: " + leilao.getMultiploLance());
            }

            int idLeilao = Tools.pedirInt("\nInsira o ID do leilão em que deseja participar " + Tools.alertaCancelar());
            if (idLeilao == -1) return;

            boolean verificarID = lanceController.verificarDisponibilidadeLeilao(leilaoEletronico, idLeilao);

            if (verificarID) {
                System.out.print("Deseja dar um Lance? (S/N)" + Tools.alertaCancelar());
                String resposta = scanner.nextLine().trim().toLowerCase();
                if (resposta.equals("s")) {
                    Leilao leilaoSelecionado = leilaoController.procurarLeilaoPorId(idLeilao);
                    double multiploLanceIncremento = leilaoSelecionado.getMultiploLance();
                    double ultimoLance = lanceController.obterUltimoLanceDoLeilao(idLeilao, leilaoEletronico);
                    double proximoLanceEsperado = ultimoLance + multiploLanceIncremento;

                    ResultadoOperacao resultado = lanceController.adicionarLanceEletronico(idLeilao, proximoLanceEsperado);
                    if (resultado.Sucesso) {
                        System.out.println("Seu lance foi aceite!");
                    } else {
                        System.out.println("Erro ao registrar o lance: " + resultado.msgErro);
                    }
                } else {
                    System.out.println("Lance não registado ");
                }
            } else {
                System.out.println("Leilão indisponível!");
            }
        } else {
            System.out.println("Não existem leilões disponíveis do tipo Eletrônico.\n");
        }
    }

    public void listarMeuLance() throws MessagingException, IOException {
        LanceController lanceController = new LanceController();
        ProdutoController produtoController = new ProdutoController();
        List<Lance> meusLances = lanceController.listarLancesDoCliente();

        if (meusLances.isEmpty()) {
            System.out.println("Nenhum lance encontrado para o cliente.");
        } else {
            System.out.println("\nOs Seus Lances:");
            System.out.println("-".repeat(130));
            System.out.printf("%-20s %-15s %-25s %-20s %-25s%n", "ID do Lance", "ID do Leilão", "Produto", "Valor (€)", "Data");
            System.out.println("-".repeat(130));
            for (Lance lance : meusLances) {
                if (lance.getEstado() != Constantes.estadosLance.DEFAULT) continue;
                String dataFormatada = Tools.formatDateTime(lance.getDataLance());
                String nomeProdutoLance = produtoController.getNomeProdutoById(lance.getIdLeilao());
                System.out.printf("%-20s %-15s %-25s %-20s %-25s%n",
                        lance.getIdLance(),
                        lance.getIdLeilao(),
                        nomeProdutoLance,
                        lance.getValorLance(),
                        dataFormatada);
            }
        }
    }

    public void listarLeiloesTerminados() throws MessagingException, IOException {
        int idCliente = Tools.clienteSessao.getIdCliente();
        LeilaoController leilaoController = new LeilaoController();
        List<Leilao> leiloes = leilaoController.listarLeiloesTerminadosComLancesDoCliente(idCliente);

        if (leiloes.isEmpty()) {
            System.out.println("Não participaste em nenhum leilão terminado.");
            return;
        }

        System.out.println("\nLeilões terminados em que participaste:");
        for (Leilao l : leiloes) {
            System.out.println("ID: " + l.getId() + " | Descrição: " + l.getDescricao());
        }
    }

    public void verDetalhesLeilaoTerminados() throws MessagingException, IOException {
        LeilaoController leilaoController = new LeilaoController();
        int idCliente = Tools.clienteSessao.getIdCliente();
        List<Leilao> leiloes = leilaoController.listarLeiloesTerminadosComLancesDoCliente(idCliente);
        LanceBLL lanceBLL = new LanceBLL();
        listarLeiloesTerminados();

        int idSelecionado = Tools.pedirOpcaoMenu("\nEscolhe o ID de um leilão para ver os detalhes " + Tools.alertaCancelar());
        if (idSelecionado == -1) {
            System.out.println("Operação cancelada.");
            return;
        }

        Leilao leilaoSelecionado = leiloes.stream()
                .filter(l -> l.getId() == idSelecionado)
                .findFirst()
                .orElse(null);

        if (leilaoSelecionado == null) {
            System.out.println("ID inválido. Certifica-te que escolheste um leilão da lista.");
            return;
        }

        lanceBLL.carregarLances();
        int idLanceVencedor = lanceBLL.selecionarLanceVencedor(leilaoSelecionado.getId());

        if (idLanceVencedor == 0) {
            System.out.println("Este leilão não teve lances, logo não há vencedor.");
            return;
        }

        String nomeVencedor = lanceBLL.obterVencedor(idLanceVencedor).getNomeUtilizador();
        Lance lanceVencedor = lanceBLL.obterLancesPorLeilao(leilaoSelecionado.getId()).stream()
                .filter(l -> l.getIdLance() == idLanceVencedor)
                .findFirst()
                .orElse(null);

        if (lanceVencedor == null) {
            System.out.println("Erro ao obter o lance vencedor.");
            return;
        }

        System.out.println("\n===== DETALHES DO LEILÃO =====");
        System.out.println("Leilão: " + leilaoSelecionado.getDescricao());
        System.out.println("Vencedor: " + nomeVencedor);
        System.out.printf("Lance vencedor: %.2f€\n", lanceVencedor.getValorLance());
    }

    private void verLeiloesDeOutrosEDarLance() throws MessagingException, IOException {
        System.out.println("\n--- Leilões Ativos de Outros Clientes ---");

        int idCliente = Tools.clienteSessao.getIdCliente();
        NegociacaoController negociacaoController = new NegociacaoController();
        List<Negociacao> leiloes = negociacaoController.listarLeiloesAtivosDeOutrosClientes(idCliente);

        if (leiloes.isEmpty()) {
            System.out.println("Nenhum leilão ativo de outros clientes disponível.");
            return;
        }

        for (Negociacao n : leiloes) {
            System.out.println("ID: " + n.getIdNegociacao());
            System.out.println("Nome: " + n.getNome());
            System.out.println("Descrição: " + n.getDescricao());
            System.out.println("Valor Pedido: " + n.getValor());
            System.out.println("------------------------------");
        }

        int idLeilao = Tools.pedirInt("Digite o ID do leilão que deseja propor: ");
        double valor = Tools.pedirDouble("Digite o valor da sua proposta: ");

        LanceBLL lanceBLL = new LanceBLL();
        ResultadoOperacao resultado = lanceBLL.fazerProposta(idLeilao, idCliente, valor);

        if (resultado.Sucesso) {
            System.out.println("Proposta enviada com sucesso: " + resultado.msgErro);
        } else {
            System.out.println("Erro: " + resultado.msgErro);
        }
    }
}
