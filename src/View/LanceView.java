package View;

import Controller.ProdutoController;
import DAL.ImportDAL;
import Controller.LanceController;
import Controller.LeilaoController;
import Model.*;
import Utils.Constantes;
import Utils.Tools;

import java.util.List;


public class LanceView {
    private final LanceController lanceController;
    private final LeilaoController leilaoController;
    private final ProdutoController produtoController;

    public LanceView(LanceController lanceController, LeilaoController leilaoController, ProdutoController produtoController) {
        this.lanceController = lanceController;
        this.leilaoController = leilaoController;
        this.produtoController = produtoController;
    }

    public void exibirMenuLance() {
        int opc;
        do {
            System.out.println("\n" + "=".repeat(5) + " MENU LANCES " + "=".repeat(5));
            System.out.println("1. Ver os Meus Lances");
            System.out.println("2. Ver Leilões Terminados");
            System.out.println("3. Dar Lance Direto");
            System.out.println("4. Dar Lance Carta Fechada");
            System.out.println("5. Dar Lance Eletrónico");
            System.out.println("0. Voltar ao menu principal...");
            System.out.print("Escolha uma opção: ");
            opc = Tools.scanner.nextInt();

            switch (opc) {
                case 1:
                    listarMeuLance();
                    break;
                case 2:
                    System.out.println("Em desenvolvimento...");
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
                case 0:
                    System.out.println("\nSair...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente...");
            }
        } while (opc != 0);

    }

    public void lanceDireto() {
        ResultadoOperacao resultado;
        System.out.println("\n===== LEILÕES VENDA DIRETA =====");

        List<Leilao> leiloesAtivos = leilaoController.listarLeiloes(true);
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

            if (verificarID) {
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

    public void lanceCartaFechada() {
        System.out.println("\n===== LEILÕES CARTA FECHADA =====");

        List<Leilao> leiloesAtivos = leilaoController.listarLeiloes(true);
        List<Leilao> leilaoCartaFechada = lanceController.listarLeiloesByTipo(leiloesAtivos, Constantes.tiposLeilao.CARTA_FECHADA);
        if (!leilaoCartaFechada.isEmpty()) {
            for (Leilao leilao : leilaoCartaFechada) {
                System.out.println("ID: " + leilao.getId() + " | Produto: " + leilao.getDescricao());
            }

            System.out.print("\nInsira o ID do leilão em que deseja participar " + Tools.alertaCancelar());
            int idLeilao = Tools.scanner.nextInt();
            if (Tools.verificarSaida(String.valueOf(idLeilao))) return;
            boolean verificarID = lanceController.verificarDisponibilidadeLeilao(leilaoCartaFechada, idLeilao);

            if (verificarID) {
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

    public void lanceEletronico() {
        System.out.println("\n===== LEILÕES ELETRONICO =====");

        List<Leilao> leiloesAtivos = leilaoController.listarLeiloes(true);
        List<Leilao> leilaoEletronico = lanceController.listarLeiloesByTipo(leiloesAtivos, Constantes.tiposLeilao.ELETRONICO);
        if (!leilaoEletronico.isEmpty()) {
            for (Leilao leilao : leilaoEletronico) {
                System.out.println("ID: " + leilao.getId() + " | Produto: " + leilao.getDescricao() + " | " + "Valor Lance: " + leilao.getMultiploLance());
            }

            System.out.print("\nInsira o ID do leilão em que deseja participar " + Tools.alertaCancelar());
            int idLeilao = Tools.scanner.nextInt();
            if (Tools.verificarSaida(String.valueOf(idLeilao))) return;
            boolean verificarID = lanceController.verificarDisponibilidadeLeilao(leilaoEletronico, idLeilao);

            if (verificarID) {
                System.out.printf("O valor do Lance é de: " + leilaoController.procurarLeilaoPorId(idLeilao).getMultiploLance());
                System.out.print("\nInsira o número de lances que deseja dar" + Tools.alertaCancelar());
                double multiploLance = leilaoController.procurarLeilaoPorId(idLeilao).getMultiploLance();
                int numLance = Tools.scanner.nextInt();
                if (Tools.verificarSaida(String.valueOf(numLance))) return;

                ResultadoOperacao resultado = lanceController.adicionarLanceEletronico(idLeilao, numLance, multiploLance);

                if (resultado.Sucesso) {
                    System.out.println("O seu Lance foi aceite");
                } else {
                    System.out.println("O lance não foi aceite! " + resultado.msgErro);
                }
            } else {
                System.out.printf("Leilão indisponível!");
            }
        } else {
            System.out.printf("Não existem leilões disponíveis do tipo Eletronico.\n");
        }
    }

    public void listarMeuLance() {
        List<Lance> meusLances = lanceController.listarLancesDoCliente();

        if (meusLances.isEmpty()) {
            System.out.println("Nenhum lance encontrado para o cliente.");
        } else {
            System.out.println("\nOs Seus Lances:");
            System.out.println("-".repeat(130));
            System.out.printf("%-20s %-15s %-25s %-20s %-25s%n", "ID do Lance", "ID do Leilão", "Produto", "Valor (€)", "Data");
            System.out.println("-".repeat(130));
            for (Lance lance : meusLances) {
                String dataFormatada = Tools.formatDateTime(lance.getDataLance());
                String nomeProdutoLance = produtoController.getNomeProdutoById(lance.getIdLeilao());
                System.out.printf("%-20s %-15s %-25s %-20s %-25s%n", lance.getIdLance(), lance.getIdLeilao(), nomeProdutoLance, lance.getValorLance(), dataFormatada);
            }
        }
    }

    public void listarLancesPorLeilao() { // PARA SER USADO PELO GESTOR
        List<Leilao> leiloesAtivos = leilaoController.listarLeiloes(true);
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
    }

}
