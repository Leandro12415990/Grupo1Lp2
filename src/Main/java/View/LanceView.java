package View;

import BLL.LanceBLL;
import Controller.*;
import DAL.UtilizadorDAL;
import Model.*;
import Utils.Constantes;
import Utils.Tools;
import com.sun.mail.imap.protocol.ID;
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
            System.out.print("Escolha uma opção: ");
            opc = Tools.scanner.nextInt();

            switch (opc) {
                case 1:
                    listarMeuLance();
                    break;
                case 2:
                    verDetalhesLeilaoTerminados();
                    break;
                case 3:
                    lanceDireto();
                    break;
                case 4:
                    lanceCartaFechada();
                    break;
                case 5:
                    lanceEletronico();
                    break;
                case 6:
                    verLeiloesDeOutrosEDarLance();
                case 0:
                    System.out.println("\nSair...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente...");
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

            System.out.print("\nInsira o ID do leilão em que deseja participar " + Tools.alertaCancelar());
            int idLeilao = Tools.scanner.nextInt();
            Tools.scanner.nextLine();
            if (Tools.verificarSaida(String.valueOf(idLeilao))) return;
            boolean verificarID = lanceController.verificarDisponibilidadeLeilao(leiloesLanceDireto, idLeilao);
            Leilao leilao = leilaoController.procurarLeilaoPorId(idLeilao);
            int idCliente = Tools.clienteSessao.getIdCliente();
            UtilizadorController utilizadorController = new UtilizadorController();
            double saldo = utilizadorController.obterSaldoCliente(idCliente);
            if (verificarID) {
                System.out.println("O seu saldo atual: " + saldo);
                System.out.print("Tem a certeza que quer dar um Lance? (S/N)" + Tools.alertaCancelar());
                String imput1 = Tools.scanner.nextLine().trim();
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
                System.out.printf("Leilão não disponível!");
            }
        } else {
            System.out.printf("Não existem leilões disponíveis do tipo Venda Direta.\n");
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

            System.out.print("\nInsira o ID do leilão em que deseja participar " + Tools.alertaCancelar());
            int idLeilao = Tools.scanner.nextInt();
            if (Tools.verificarSaida(String.valueOf(idLeilao))) return;
            boolean verificarID = lanceController.verificarDisponibilidadeLeilao(leilaoCartaFechada, idLeilao);
            int idCliente = Tools.clienteSessao.getIdCliente();
            UtilizadorController utilizadorController = new UtilizadorController();
            double saldo = utilizadorController.obterSaldoCliente(idCliente);

            if (verificarID) {
                System.out.println("O seu saldo atual: " + saldo);
                System.out.print("Insira o valor do lance " + Tools.alertaCancelar());
                double valorLance = Tools.scanner.nextDouble();
                if (Tools.verificarSaida(String.valueOf(valorLance))) return;

                ResultadoOperacao resultado = lanceController.adicionarLanceCartaFechada(idLeilao, valorLance);

                if (resultado.Sucesso) {
                    System.out.println("O seu Lance foi aceite");
                } else {
                    System.out.println(resultado.msgErro);
                }
            } else {
                System.out.printf("Leilão indisponível!");
            }
        } else {
            System.out.printf("Não existem leilões disponíveis do tipo Carta Fechada.\n");
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

            System.out.print("\nInsira o ID do leilão em que deseja participar " + Tools.alertaCancelar());
            int idLeilao = Tools.scanner.nextInt();
            if (Tools.verificarSaida(String.valueOf(idLeilao))) return;

            boolean verificarID = lanceController.verificarDisponibilidadeLeilao(leilaoEletronico, idLeilao);

            if (verificarID) {
                Leilao leilaoSelecionado = leilaoController.procurarLeilaoPorId(idLeilao);
                double multiploLanceIncremento = leilaoSelecionado.getMultiploLance();

                double ultimoLance = lanceController.obterUltimoLanceDoLeilao(idLeilao, leilaoEletronico);
                double proximoLanceEsperado = ultimoLance + multiploLanceIncremento;
                int idCliente = Tools.clienteSessao.getIdCliente();
                UtilizadorController utilizadorController = new UtilizadorController();
                double saldo = utilizadorController.obterSaldoCliente(idCliente);

                System.out.printf("O valor atual do último lance é: %.2f\n", ultimoLance);
                System.out.printf("Seu novo lance deve ser exatamente: %.2f\n", proximoLanceEsperado);
                System.out.println("O seu saldo atual: " + saldo);
                System.out.print("Deseja dar este Lance? (S/N)" + Tools.alertaCancelar());
                String resposta = Tools.scanner.next().trim().toLowerCase();
                if (resposta.equals("s")) {
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


    /*public void listarLancesPorLeilao() throws MessagingException, IOException {
        LanceController lanceController = new LanceController();
        LeilaoController leilaoController = new LeilaoController();// PARA SER USADO PELO GESTOR
        List<Leilao> leiloesAtivos = leilaoController.listarLeiloes(Tools.estadoLeilao.ATIVO);
        List<Leilao> leilaoEletronicoAtivo = lanceController.listarLeiloesByTipo(leiloesAtivos, Constantes.tiposLeilao.ELETRONICO);
        //LeilaoView.exibirLeiloes(leilaoEletronicoAtivo);
        System.out.print("\nInsira o ID do leilão para visualizar os lances: ");
        int idLeilao = Tools.scanner.nextInt();

        List<Lance> lances = lanceController.obterLancesPorLeilao(idLeilao);

        if (lances.isEmpty()) {
            System.out.println("Nenhum lance encontrado para este leilão.");
        } else {
            System.out.println("\nLances para o Leilão ID: " + idLeilao);
            System.out.println("------------------------------------------------");
            for (Lance lance : lances) {
                System.out.println("ID do Lance: " + lance.getIdLance());
                System.out.println("Valor: " + lance.getValorLance() + "€");
                System.out.println("Cliente: " + lance.getIdCliente());
                System.out.println("Data: " + lance.getDataLance());
                System.out.println("------------------------------------------------");
            }
        }
    }*/

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

        Leilao leilaoSelecionado = null;
        for (Leilao l : leiloes) {
            if (l.getId() == idSelecionado) {
                leilaoSelecionado = l;
                break;
            }
        }

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
        List<Lance> lancesDoLeilao = lanceBLL.obterLancesPorLeilao(leilaoSelecionado.getId());

        Lance lanceVencedor = null;
        for (Lance l : lancesDoLeilao) {
            if (l.getIdLance() == idLanceVencedor) {
                lanceVencedor = l;
                break;
            }
        }

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

        int idLeilao;
        while (true) {
            System.out.print("Digite o ID do leilão que deseja propor: ");
            if (scanner.hasNextInt()) {
                idLeilao = scanner.nextInt();
                scanner.nextLine();
                break;
            } else {
                System.out.println("Entrada inválida! Insira um número inteiro.");
                scanner.nextLine();
            }
        }

        double valor;
        while (true) {
            System.out.print("Digite o valor da sua proposta: ");
            if (scanner.hasNextDouble()) {
                valor = scanner.nextDouble();
                scanner.nextLine();
                break;
            } else {
                System.out.println("Entrada inválida! Insira um valor numérico.");
                scanner.nextLine();
            }
        }

        LanceBLL lanceBLL = new LanceBLL();
        ResultadoOperacao resultado = lanceBLL.fazerProposta(idLeilao, idCliente, valor);

        if (resultado.Sucesso) {
            System.out.println("Proposta enviada com sucesso: " + resultado.msgErro);
        } else {
            System.out.println("Erro: " + resultado.msgErro);
        }
    }

}
