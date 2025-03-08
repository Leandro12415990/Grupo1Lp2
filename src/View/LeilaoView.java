package View;

import Model.Leilao;
import Utils.Tools;
import Controller.LeilaoController;

import java.time.LocalDate;
import java.util.List;

public class LeilaoView {
    public static void exibirMenuLeiloes() {
        int opc;
        do {
            System.out.println("\n"+"=".repeat(5) + " MENU LEILÕES " + "=".repeat(5));
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
                    /*editLeilao();*/
                    System.out.println("Funcionalidade em desenvolvimento");
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
        System.out.print("Insira o produto que pretende leiloar: ");
        String produto = Tools.scanner.next();
        System.out.print("Insira a descrição do leilão: ");
        String descricao = Tools.scanner.next();

        String tipoLeilao = null;
        int opc;
        do {
            System.out.println("\n1. Leilão Eletrónico");
            System.out.println("2. Leilão Carta Fechada");
            System.out.println("3. Leilão Venda Direta");
            System.out.print("Escolha o tipo de leilão: ");
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
                default:
                    System.out.println("Opção inválida. Tente novamente...");
            }

        } while (opc < 1 || opc > 3);

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
        if (opc == 1) {
            System.out.print("Insira o múltiplo de lance: ");
            multiploLance = Tools.scanner.nextDouble();
        }

        String estado = null;
        if (dataFim != null && dataFim.isBefore(LocalDate.now())) {
            estado = "Fechado";
        } else {
            estado = "Ativo";
        }
        // Chamada ao método criarLeiloes()
        boolean criado = LeilaoController.criarLeiloes(0, produto, descricao, tipoLeilao, dataInicio, dataFim, valorMin, valorMax, multiploLance, estado);

        if (criado) {
            System.out.println("Leilão criado com sucesso!");
        } else {
            System.out.println("Erro ao criar leilão. Verifique os dados inseridos.");
        }
    }

    private static void listaLeiloes() {
        LeilaoController.listarLeiloes();  // O Controller chama a BLL
    }

    public static void exibirLeiloes(List<Leilao> leiloes) {
        System.out.println("\n" + "=".repeat(5) + " LISTAGEM DE LEILÕES " + "=".repeat(5));
        System.out.printf("%-8s %-30s %-30s %-25s %-30s %-30s %-30s %-30s %-25s %-10s\n",
                "Id", "Produto", "Descrição", "TipoLeilão", "Data Início", "Data Fim", "Valor Minimo", "Valor Maximo", "Multiplo de Lance", "Estado");
        System.out.println("-".repeat(260));

        for (Leilao leilao : leiloes) {
            System.out.printf("%-8s %-30s %-30s %-25s %-30s %-30s %-30s %-30s %-25s %-10s\n",
                    leilao.getId(),
                    leilao.getNomeProduto(),
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
        System.out.print("Introduza o Id do Leilão: ");
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
        System.out.println("Produto: " + leilao.getNomeProduto());
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
        System.out.print("Introduza o Id do Leilao: ");
        int id = Tools.scanner.nextInt();

        boolean sucesso = LeilaoController.eliminarLeilao(id);
        if (sucesso) {
            System.out.println("Leilão eliminado com sucesso.");
        } else {
            System.out.println("Erro ao eliminar leilão.");
        }

    }
}
