package View;

import BLL.LanceBLL;
import Controller.EstatisticaController;
import Model.Lance;
import Model.Leilao;
import Utils.Constantes;
import Utils.Tools;

import java.time.Period;
import java.util.List;

public class EstatisticaView {
    public static void exibirMenuListagem() {
        int opc;
        do {
            System.out.println("\n" + "=".repeat(5) + " MENU LISTAGEM " + "=".repeat(5));
            System.out.println("1. Estatisticas Por Leilão");
            System.out.println("2. Estatisticas Globais");
            System.out.println("3. Estatisticas Por Tipo Leilão");
            System.out.println("0. Voltar ao menu principal...");
            System.out.print("Escolha uma opção: ");
            opc = Tools.scanner.nextInt();
            switch (opc) {
                case 1:
                    break;
                case 2:
                    exibirMenuGlobal();
                    break;
                case 3:
                    exibirMenuPorTipo();
                    break;
                case 0:
                    System.out.println("\nSair...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente...");
            }
        } while (opc != 0);
    }

    public static void exibirMenuGlobal() {
        int opc;
        do {
            System.out.println("\n" + "=".repeat(5) + " MENU LISTAGEM " + "=".repeat(5));
            System.out.println("1. Quantidade de leilôes terminados.");
            System.out.println("2. Leilão que mais tempo esteve ativo.");
            System.out.println("3. Leilão com mais lances.");
            System.out.println("4. Média de tempo para o lance acontecer.");
            System.out.println("5. Leilões sem lances.");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opc = Tools.scanner.nextInt();
            switch (opc) {
                case 1:
                    exibirLeiloesFinalizados();
                    break;
                case 2:
                    mostrarLeilaoMaisTempoAtivo();
                    break;
                case 3:
                    mostrarLeilaoComMaisLances();
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente...");
            }
        } while (opc != 0);
    }

    public static void exibirMenuPorTipo() {
        int opc;
        do {
            System.out.println("\n=== ESCOLHA O TIPO DE LEILÃO ===");
            System.out.println("1. Eletrónico");
            System.out.println("2. Carta Fechada");
            System.out.println("3. Venda Direta");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opc = Tools.scanner.nextInt();

            switch (opc) {
                case 1:
                    menuAcoesEletronico();
                    break;
                case 2:
                    menuAcoesCartaFechada();
                    break;
                case 3:
                    menuAcoesVendaDireta();
                    break;
                case 0:
                    System.out.println("A sair...");
                    break;
                default: System.out.println("Opção inválida.");
            }

        } while (opc != 0);
    }

    public static void menuAcoesEletronico() {
        int opc;
        do {
            System.out.println("\n=== AÇÕES - LEILÕES ELETRÓNICOS ===");
            System.out.println("1. Ver quantidade de leilões fechados");
            System.out.println("2. Listar leilão mais tempo fechado");
            System.out.println("3. Leilão com mais lances.");
            System.out.println("4. Média de tempo para o lance acontecer.");
            System.out.println("5. Leilões sem lances.");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opc = Tools.scanner.nextInt();

            switch (opc) {
                case 1:
                    exibirContagemPorTipo(Constantes.tiposLeilao.ELETRONICO);
                    break;
                case 2:
                    mostrarLeilaoMaisTempoPorTipo(Constantes.tiposLeilao.ELETRONICO);
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;

                case 0:
                    System.out.println("A voltar...");
                    break;
                default: System.out.println(" Opção inválida.");
            }

        } while (opc != 0);
    }

    public static void menuAcoesCartaFechada() {
        int opc;
        do {
            System.out.println("\n=== AÇÕES - LEILÕES CARTA FECHADA ===");
            System.out.println("1. Ver quantidade de leilões fechados");
            System.out.println("2. Listar leilão mais tempo fechado");
            System.out.println("3. Leilão com mais lances.");
            System.out.println("4. Média de tempo para o lance acontecer.");
            System.out.println("5. Leilões sem lances.");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opc = Tools.scanner.nextInt();

            switch (opc) {
                case 1:
                    exibirContagemPorTipo(Constantes.tiposLeilao.CARTA_FECHADA);
                    break;
                case 2:
                    mostrarLeilaoMaisTempoPorTipo(Constantes.tiposLeilao.CARTA_FECHADA);
                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 0:
                    System.out.println("A voltar...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }

        } while (opc != 0);
    }

    public static void menuAcoesVendaDireta() {
        int opc;
        do {
            System.out.println("\n=== AÇÕES - LEILÕES VENDA DIRETA ===");
            System.out.println("1. Ver quantidade de leilões fechados");
            System.out.println("2. Listar leilão mais tempo fechado");
            System.out.println("3. Leilão com mais lances.");
            System.out.println("4. Média de tempo para o lance acontecer.");
            System.out.println("5. Leilões sem lances.");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opc = Tools.scanner.nextInt();

            switch (opc) {
                case 1:
                    exibirContagemPorTipo(Constantes.tiposLeilao.VENDA_DIRETA);
                    break;
                case 2:
                    mostrarLeilaoMaisTempoPorTipo(Constantes.tiposLeilao.VENDA_DIRETA);
                    break;
                case 3:
                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 0:
                    System.out.println("A voltar...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }

        } while (opc != 0);
    }

    public static void exibirContagemPorTipo(int idTipo) {
        int total = EstatisticaController.contarLeiloesFechadosPorTipo(idTipo);
        String tipoLeilaoStr = Tools.tipoLeilao.fromCodigo(idTipo).name();
        if (total == 0) {
            System.out.println("Não existem leilões fechados do tipo \"" + tipoLeilaoStr + "\".");
        } else {
            System.out.println("Total de leilões fechados do tipo \"" + tipoLeilaoStr + "\": " + total);
        }
    }

    public static void exibirLeiloesFinalizados() {
            System.out.println("\n" + "=".repeat(5) + " LEILÕES FECHADOS " + "=".repeat(5));

            int total = EstatisticaController.contarLeilaoGlobal();

            if (total == 0) {
                System.out.println("Não existem leilões com estado 'Fechado'.");
            } else {
                System.out.println("A quantidade de leilões terminados é: " + total);
                System.out.println("\n Lista de leilões fechados:\n");

                List<String> linhas = EstatisticaController.listarLeiloesFechadosFormatados();
                for (String linha : linhas) {
                    System.out.println(linha);
                }
            }
        }

    public static void mostrarLeilaoMaisTempoAtivo() {
        Leilao leilao = EstatisticaController.getLeilaoMaisTempoAtivo();

        if (leilao == null) {
            System.out.println("Não existem leilões válidos.");
            return;
        }

        Period tempo = Period.between(leilao.getDataInicio(), leilao.getDataFim());

        System.out.println("\n=== Leilão com mais tempo ativo ===");
        System.out.println("ID: " + leilao.getId());
        System.out.println("Descrição: " + leilao.getDescricao());
        System.out.println("Tempo ativo: " + tempo.getYears() + " anos, " +
                tempo.getMonths() + " meses, " +
                tempo.getDays() + " dias");
    }

    public static void mostrarLeilaoMaisTempoPorTipo(int idTipo) {
        Leilao leilao = EstatisticaController.getLeilaoTipoMaisTempoAtivo(idTipo);

        if (leilao == null) {
            System.out.println("Nenhum leilão válido encontrado para este tipo.");
            return;
        }

        Period tempo = Period.between(leilao.getDataInicio(), leilao.getDataFim());

        System.out.println("\n=== Leilão com mais tempo ativo (por tipo) ===");
        System.out.println("ID: " + leilao.getId());
        System.out.println("Descrição: " + leilao.getDescricao());
        System.out.println("Tempo ativo: " + tempo.getYears() + " anos, " +
                tempo.getMonths() + " meses, " + tempo.getDays() + " dias");
    }

    public static void mostrarLeilaoComMaisLances() {
        int id = EstatisticaController.getIdLeilaoComMaisLances();

        if (id == -1) {
            System.out.println("Não existem lances registados.");
            return;
        }

        int total = 0;
        List<Lance> lances = LanceBLL.carregarLance();
        for (Lance l : lances) {
            if (l.getIdLeilao() == id) total++;
        }

        System.out.println("\n=== Leilão com mais lances ===");
        System.out.println("ID do Leilão: " + id);
        System.out.println("Total de lances: " + total);
    }






}
