package View;

import BLL.LeilaoBLL;
import Controller.LanceController;
import Controller.LeilaoController;
import Model.Lance;
import Model.Leilao;
import Model.ResultadoOperacao;
import Utils.Constantes;
import Utils.Tools;

import javax.tools.Tool;
import java.time.LocalDate;
import java.util.List;

import static BLL.LeilaoBLL.listarLeiloes;

public class LanceView {
    public static void exibirMenuLance() {
        int opc;
        do {
            System.out.println("\n" + "=".repeat(5) + " MENU LANCES " + "=".repeat(5));
            System.out.println("1. Ver os meus Lances");
            System.out.println("2. Ver Lances por Leilão");
            System.out.println("3. Ver Leilões Terminados");
            System.out.println("4. Dar Lance direto");
            System.out.println("5. Dar Lance Carta Fechada");
            System.out.println("6. Dar Lance Eletronico");
            System.out.println("0. Voltar ao menu principal...");
            System.out.print("Escolha uma opção: ");
            opc = Tools.scanner.nextInt();

            switch (opc) {
                case 1:
                   listarMeuLance();
                    break;
                case 2:
                    listarLancesPorLeilao();
                    break;
                case 3:
                    System.out.println("Em desenvolvimento...");
                    break;
                case 4:
                    lanceDireto();
                    break;
                case 5:
                    lanceCartaFechada();
                    break;
                case 6:
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

    public static void lanceDireto() {
        ResultadoOperacao resultado;
        System.out.println("\n===== LEILÕES VENDA DIRETA =====");

        List<Leilao> leiloesAtivos = listarLeiloes(true);
        List<Leilao> leiloesLanceDireto = LanceController.listarLeiloesByTipo(leiloesAtivos, Constantes.tiposLeilao.VENDA_DIRETA);
        if (!leiloesLanceDireto.isEmpty()) {
            for (Leilao leilao : leiloesLanceDireto) {
                System.out.println("ID: " + leilao.getId() + " | Produto: " + leilao.getIdProduto() + " | Valor Lance: " + leilao.getValorMinimo());
            }

            System.out.print("\nInsira o ID do leilão em que deseja participar " + Tools.alertaCancelar());
            int idLeilao = Tools.scanner.nextInt();
            if (Tools.verificarSaida(String.valueOf(idLeilao))) return;
            boolean verificarID = LanceController.verificarDisponibilidadeLeilao(leiloesLanceDireto, idLeilao);
            Leilao leilao = LeilaoBLL.procurarLeilaoPorId(idLeilao);
            if (verificarID) {
                System.out.print("Tem a certeza que quer dar um Lance? (S/N)" + Tools.alertaCancelar());
                char opc = Character.toUpperCase(Tools.scanner.next().charAt(0));
                if (Tools.verificarSaida(String.valueOf(opc))) return;
                if (opc == 'S') {
                    Double valorLance = leilao.getValorMinimo();
                    resultado = LanceController.adicionarLanceDireto(idLeilao, valorLance);
                    if (resultado.Sucesso) {
                        System.out.println("✅ aceite");
                    } else {
                        System.out.println("❌ " + resultado.msgErro);
                    }
                }else if (opc == 'N'){
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

    public static void lanceCartaFechada() {
        System.out.println("\n===== LEILÕES CARTA FECHADA =====");

        List<Leilao> leiloesAtivos = listarLeiloes(true);
        List<Leilao> leilaoCartaFechada = LanceController.listarLeiloesByTipo(leiloesAtivos, Constantes.tiposLeilao.CARTA_FECHADA);
        if (!leilaoCartaFechada.isEmpty()) {
            for (Leilao leilao : leilaoCartaFechada) {
                System.out.println("ID: " + leilao.getId() + " | Produto: " + leilao.getIdProduto());
            }

            System.out.print("\nInsira o ID do leilão em que deseja participar " + Tools.alertaCancelar());
            int idLeilao = Tools.scanner.nextInt();
            if (Tools.verificarSaida(String.valueOf(idLeilao))) return;
            boolean verificarID = LanceController.verificarDisponibilidadeLeilao(leilaoCartaFechada, idLeilao);

            if (verificarID) {
                System.out.print("Insira o valor do lance " + Tools.alertaCancelar());
                double valorLance = Tools.scanner.nextDouble();
                if (Tools.verificarSaida(String.valueOf(valorLance))) return;

                ResultadoOperacao resultado = LanceController.adicionarLanceDireto(idLeilao, valorLance);

                if (resultado.Sucesso) {
                    System.out.println("✅ aceite");
                } else {
                    System.out.println("❌ " + resultado.msgErro);
                }
            } else {
                System.out.printf("Leilão não disponível!");
            }
        } else {
            System.out.printf("Não existem leilões disponíveis do tipo Carta Fechada.\n");
        }
    }

    public static void lanceEletronico() {
        System.out.println("\n===== LEILÕES ELETRONICO =====");

        List<Leilao> leiloesAtivos = listarLeiloes(true);
        List<Leilao> leilaoEletronico = LanceController.listarLeiloesByTipo(leiloesAtivos, Constantes.tiposLeilao.ELETRONICO);
        if (!leilaoEletronico.isEmpty()) {
            for (Leilao leilao : leilaoEletronico) {
                System.out.println("ID: " + leilao.getId() + " | Produto: " + leilao.getIdProduto());
            }

            System.out.print("\nInsira o ID do leilão em que deseja participar " + Tools.alertaCancelar());
            int idLeilao = Tools.scanner.nextInt();
            if (Tools.verificarSaida(String.valueOf(idLeilao))) return;
            boolean verificarID = LanceController.verificarDisponibilidadeLeilao(leilaoEletronico, idLeilao);

            if (verificarID) {
                System.out.printf("O valor do Lance é de: " + LeilaoController.procurarLeilaoPorId(idLeilao).getMultiploLance());
                System.out.print("\nInsira o número de lances que deseja dar" + Tools.alertaCancelar());
                double multiploLance = LeilaoController.procurarLeilaoPorId(idLeilao).getMultiploLance();
                int numLance = Tools.scanner.nextInt();
                if (Tools.verificarSaida(String.valueOf(numLance))) return;

                ResultadoOperacao resultado = LanceController.adicionarLanceEletronico(idLeilao, numLance, multiploLance);

                if (resultado.Sucesso) {
                    System.out.println("✅ aceite");
                } else {
                    System.out.println("❌ " + resultado.msgErro);
                }
            } else {
                System.out.printf("Leilão não disponível!");
            }
        } else {
            System.out.printf("Não existem leilões disponíveis do tipo Eletronico.\n");
        }
    }

    public static void listarMeuLance() {
        List<Lance> meusLances = LanceController.listarLancesDoCliente();

        if (meusLances.isEmpty()) {
            System.out.println("Nenhum lance encontrado para o cliente.");
        } else {
            System.out.println("\nOs Seus Lances:");
            System.out.println("-".repeat(58));
            System.out.printf("%-20s %-15s %-20s%n", "ID do Lance", "Valor (€)", "Data");
            System.out.println("-".repeat(58));
            for (Lance lance : meusLances) {
                String dataFormatada = Tools.formatDateTime(lance.getDataLance());
                System.out.printf("%-20s %-15s %-20s%n", lance.getIdLance(), lance.getValorLance(), dataFormatada);

            }
        }
    }

    public static void listarLancesPorLeilao() {
        List<Leilao> leiloesAtivos = listarLeiloes(true);
        List<Leilao> leilaoEletronicoAtivo = LanceController.listarLeiloesByTipo(leiloesAtivos, Constantes.tiposLeilao.ELETRONICO);
        LeilaoView.exibirLeiloes(leilaoEletronicoAtivo);
        System.out.print("\nInsira o ID do leilão para visualizar os lances: ");
        int idLeilao = Tools.scanner.nextInt();

        List<Lance> lances = LanceController.obterLancesPorLeilao(idLeilao);

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
