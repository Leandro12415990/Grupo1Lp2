package View;

import Controller.ProdutoController;
import Model.ClienteSessao;
import Model.Leilao;
import Model.ResultadoOperacao;
import Utils.Tools;
import Controller.LeilaoController;

import java.time.LocalDate;
import java.util.List;

public class LeilaoView {
    public static void exibirMenuLeiloes() {
        int opc;
        do {
            System.out.println("\n" + "=".repeat(5) + " MENU LEILÕES " + "=".repeat(5));
            System.out.println("1. Adicionar Leilão");
            System.out.println("2. Editar Leilão");
            System.out.println("3. Consultar Leilão");
            System.out.println("4. Eliminar Leilão");
            System.out.println("5. Listar Leilão");
            System.out.println("0. Voltar ao menu principal...");
            System.out.print("Escolha uma opção: ");
            opc = Tools.scanner.nextInt();
            switch (opc) {
                case 1:
                    criarLeilao();
                    break;
                case 2:
                    editarLeilao();
                    break;
                case 3:
                    procurarLeilao();
                    break;
                case 4:
                    eliminarLeilao();
                    break;
                case 5:
                    listaLeiloes();
                    break;
                case 0:
                    System.out.println("\nSair...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente...");
            }
        } while (opc != 0);
    }

    private static void criarLeilao() {
        System.out.println("\nCRIAÇÃO DE UM LEILÃO\n");
        System.out.print("⚠ Produto disponíveis para leiloar ⚠");
        ProdutoView.listarProduto();
        System.out.print("\nIntroduza o ID do produto que pretende leiloar: ");
        int idProduto = Tools.scanner.nextInt();
        System.out.print("Insira a descrição do leilão: ");
        String descricao = Tools.scanner.next();

        String tipoLeilao = null;
        int opcTipoLeilao;
        do {
            System.out.println("\n1. Leilão Eletrónico");
            System.out.println("2. Leilão Carta Fechada");
            System.out.println("3. Leilão Venda Direta");
            System.out.print("Escolha o tipo de leilão: ");
            opcTipoLeilao = Tools.scanner.nextInt();
            switch (opcTipoLeilao) {
                case 1:
                    tipoLeilao = "ELETRONICO";
                    break;
                case 2:
                    tipoLeilao = "CARTA FECHADA";
                    break;
                case 3:
                    tipoLeilao = "VENDA DIRETA";
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente...");
            }

        } while (opcTipoLeilao < 1 || opcTipoLeilao > 3);

        System.out.print("Insira a data de início do leilão (dd/MM/yyyy): ");
        String dataInicioStr = Tools.scanner.next();
        LocalDate dataInicio = Tools.parseDate(dataInicioStr);

        System.out.print("Insira a data de fim do leilão (dd/MM/yyyy) ou pressione ENTER para não definir: ");
        Tools.scanner.nextLine();
        String dataFimStr = Tools.scanner.nextLine();
        LocalDate dataFim = dataFimStr.isEmpty() ? null : Tools.parseDate(dataFimStr);

        System.out.print("Insira o valor mínimo: ");
        double valorMin = Tools.scanner.nextDouble();

        System.out.print("Insira o valor máximo (ou -1 se não quiser definir): ");
        double valorMax = Tools.scanner.nextDouble();

        double multiploLance = 0;
        if (opcTipoLeilao == 1) {
            System.out.print("Insira o valor de cada lance: ");
            multiploLance = Tools.scanner.nextDouble();
        }

        String estado;
        if (dataFim != null && dataFim.isBefore(LocalDate.now())) {
            estado = "FECHADO";
        } else {
            estado = "ATIVO";
        }
        // Chamada ao método criarLeiloes()
        ResultadoOperacao resultado = LeilaoController.criarLeiloes(0, idProduto, descricao, tipoLeilao, dataInicio, dataFim, valorMin, valorMax, multiploLance, estado);

        if (resultado.Sucesso) {
            System.out.println("Leilão criado com sucesso!");
        } else {
            System.out.println(resultado.msgErro);
        }
    }

    static void listaLeiloes() {
        LeilaoController.listarLeiloes();
    }

    public static void exibirLeiloes(List<Leilao> leiloes) {
        System.out.println("\n" + "=".repeat(5) + " LISTAGEM DE LEILÕES " + "=".repeat(5));
        System.out.printf("%-8s %-30s %-30s %-25s %-30s %-30s %-30s %-30s %-25s %-10s\n",
                "Id", "Produto", "Descrição", "TipoLeilão", "Data Início", "Data Fim", "Valor Minimo", "Valor Maximo", "Multiplo de Lance", "Estado");
        System.out.println("-".repeat(260));

        for (Leilao leilao : leiloes) {
            System.out.printf("%-8s %-30s %-30s %-25s %-30s %-30s %-30s %-30s %-25s %-10s\n",
                    leilao.getId(),
                    nomeProduto(leilao.getIdProduto()),
                    leilao.getDescricao(),
                    leilao.getTipoLeilao(),
                    leilao.getDataInicio() != null ? Tools.FORMATTER.format(leilao.getDataInicio()) : "N/A",
                    leilao.getDataFim() != null ? Tools.FORMATTER.format(leilao.getDataFim()) : "N/A",
                    leilao.getValorMinimo(),
                    leilao.getValorMaximo() != null && leilao.getValorMaximo() != -1.0 ? leilao.getValorMaximo() : "N/A",
                    leilao.getMultiploLance() != null && leilao.getMultiploLance() != 0 ? leilao.getMultiploLance() : "N/A",
                    leilao.getEstado());
        }
    }

    private static void procurarLeilao() {
        System.out.println("\nPROCURAR UM LEILÃO");
        listaLeiloes();
        System.out.print("\nIntroduza o ID do Leilão que pretende consultar: ");
        int id = Tools.scanner.nextInt();

        Leilao leilao = LeilaoController.procurarLeilaoPorId(id);

        if (leilao != null) {
            exibirLeilaoDetalhado(leilao);
        } else {
            System.out.println("O Leilão com o Id " + id + " não foi encontrado.");
        }
    }

    private static void exibirLeilaoDetalhado(Leilao leilao) {
        System.out.println("\nDETALHES DO LEILÃO COM O ID " + leilao.getId());
        System.out.println("Produto: " + nomeProduto(leilao.getIdProduto()));
        System.out.println("Descrição: " + leilao.getDescricao());
        System.out.println("Tipo Leilão: " + leilao.getTipoLeilao());
        System.out.println("Data Início: " + Tools.FORMATTER.format(leilao.getDataInicio()));
        System.out.println("Data Fim: " + (leilao.getDataFim() != null ? Tools.FORMATTER.format(leilao.getDataFim()) : "N/A"));
        System.out.println("Valor Minímo: " + leilao.getValorMinimo());
        System.out.println("Valor Máximo: " + (leilao.getValorMaximo() != null && leilao.getValorMaximo() != -1.0 ? leilao.getValorMaximo() : "N/A"));
        System.out.println("Múltiplo de Lance: " + (leilao.getMultiploLance() != null && leilao.getMultiploLance() != 0 ? leilao.getMultiploLance() : "N/A"));
        System.out.println("Estado: " + leilao.getEstado());
    }

    private static void eliminarLeilao() {
        System.out.println("\nELIMINAÇÃO DE UM LEILÃO");
        listaLeiloes();
        System.out.print("\nIntroduza o ID do Leilão que pretende eliminar: ");
        int id = Tools.scanner.nextInt();

        Leilao leilao = LeilaoController.procurarLeilaoPorId(id);
        if (leilao != null) {
            exibirLeilaoDetalhado(leilao);
            System.out.println("\nTem a certeza que pretende eliminar o leilão com o Id " + id + "? (S/N)");
            char opc = Character.toUpperCase(Tools.scanner.next().charAt(0));
            switch (opc) {
                case 'S':
                    boolean sucesso = LeilaoController.eliminarLeilao(id);
                    if (sucesso) {
                        System.out.println("Leilão eliminado com sucesso.");
                    } else {
                        System.out.println("Erro ao eliminar leilão.");
                    }
                    break;
                case 'N':
                    System.out.println("Eliminação cancelada.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } else {
            System.out.println("[ERRO] Leilão não encontrado.");
        }


    }

    private static void editarLeilao() {
        System.out.println("\nEDIÇÃO DE UM LEILÃO");
        listaLeiloes();
        System.out.print("\nIntroduza o ID do Leilão que pretende editar: ");
        int id = Tools.scanner.nextInt();

        Leilao leilao = LeilaoController.procurarLeilaoPorId(id);


        if (leilao != null) {
            System.out.println("Introduza os novos dados: \n");
            System.out.print("⚠ Produto disponíveis para leiloar ⚠");
            ProdutoView.listarProduto();
            System.out.print("\nNovo o ID do produto que pretende leiloar (ou -1 se não quiser alterar): ");
            int idProduto = Tools.scanner.nextInt();

            System.out.print("Nova a descrição do leilão (ou pressione ENTER para não alterar): ");
            //Tools.scanner.nextLine();
            String descricao = Tools.scanner.next();

            String tipoLeilao = null;
            int opc;
            do {
                System.out.println("\n1. Leilão Eletrónico");
                System.out.println("2. Leilão Carta Fechada");
                System.out.println("3. Leilão Venda Direta");
                System.out.println("0. Não alterar o tipo de leilão");
                System.out.print("Escolha novo tipo de leilão: ");
                //Tools.scanner.nextLine();
                opc = Tools.scanner.nextInt();
                switch (opc) {
                    case 1:
                        tipoLeilao = "ELETRONICO";
                        break;
                    case 2:
                        tipoLeilao = "CARTA FECHADA";
                        break;
                    case 3:
                        tipoLeilao = "VENDA DIRETA";
                        break;
                    case 0:
                        tipoLeilao = leilao.getTipoLeilao();
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente...");
                }

            } while (opc < 0 || opc > 3);

            System.out.print("\nNova a data de início do leilão (dd/MM/yyyy) ou pressione ENTER para não alterar: ");
            Tools.scanner.nextLine();
            String dataInicioStr = Tools.scanner.nextLine();
            LocalDate dataInicio = dataInicioStr.isEmpty() ? leilao.getDataInicio() : Tools.parseDate(dataInicioStr);

            System.out.print("Nova a data de fim do leilão (dd/MM/yyyy) ou pressione ENTER para não alterar: ");
            //Tools.scanner.nextLine();
            String dataFimStr = Tools.scanner.nextLine();
            LocalDate dataFim = dataFimStr.isEmpty() ? leilao.getDataFim() : Tools.parseDate(dataFimStr);

            System.out.print("Novo o valor mínimo (ou -1 se não quiser alterar): ");
            double valorMin = Tools.scanner.nextDouble();

            System.out.print("Novo o valor máximo (ou -1 se não quiser alterar): ");
            double valorMax = Tools.scanner.nextDouble();

            double multiploLance = 0;
            if (opc == 1 || tipoLeilao.equals("ELETRONICO")) {
                System.out.print("Novo o múltiplo de lance (ou -1 se não quiser alterar): ");
                multiploLance = Tools.scanner.nextDouble();
            }

            String estado;
            if (dataFim != null && dataFim.isBefore(LocalDate.now())) {
                estado = "FECHADO";
            } else {
                estado = "ATIVO";
            }

            boolean sucesso = LeilaoController.editarLeilao(id, idProduto, descricao, tipoLeilao, dataInicio, dataFim, valorMin, valorMax, multiploLance, estado);
            if (sucesso) {
                System.out.println("Leilão editado com sucesso!");
            } else {
                System.out.println("Warning: Erro a editar o leilão!");
            }
        } else {
            System.out.println("Leilão não encontrado.");
        }
    }

    private static String nomeProduto(int idProduto){
        return ProdutoController.getNomeProdutoById(idProduto);
    }
}
