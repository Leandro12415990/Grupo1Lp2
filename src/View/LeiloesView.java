package View;

import Utils.Tools;
import Controller.LeilosController;
import java.time.LocalDate;

public class LeiloesView {
    public static void exibirMenuLeiloes() {
        int opc;
        do {
            System.out.println("\nMenu Leiloes\n");
            System.out.println("1. Adicionar Leilão");
            System.out.println("2. Editar Leilão");
            System.out.println("3. Consultar Leilão");
            System.out.println("4. Eliminar Leilão");
            System.out.println("5. Listar Leilão");
            System.out.println("0. Voltar ao menu principal...");
            opc = Tools.scanner.nextInt();
            switch (opc) {
                case 1:
                    criarLeilao();
                    break;
                case 2:
                    /*editLeilao();*/
                    break;
                case 3:
                    /*procurarLeilao();*/
                    break;
                case 4:
                    /*eliminarLeilao();*/
                    break;
                case 5:
                    /*listaLeiloes();*/
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
        System.out.println("\nCriação de um Leilão\n");
        System.out.println("Insira o produto que pretende leiloar: ");
        String produto = Tools.scanner.next();
        System.out.println("Insira a descrição do leilão: ");
        String descricao = Tools.scanner.next();

        int tipoLeilao;
        do {
            System.out.println("Escolha o tipo de leilão: ");
            System.out.println("\t1. Leilão Eletrónico");
            System.out.println("\t2. Leilão Carta Fechada");
            System.out.println("\t3. Leilão Venda Direta");
            tipoLeilao = Tools.scanner.nextInt();
            if (tipoLeilao < 1 || tipoLeilao > 3) {
                System.out.println("Opção inválida. Tente novamente...");
            }
        } while (tipoLeilao < 1 || tipoLeilao > 3);

        System.out.println("Insira a data de início do leilão (yyyy-MM-dd): ");
        String dataInicioStr = Tools.scanner.next();
        LocalDate dataInicio = Tools.parseDate(dataInicioStr);

        System.out.println("Insira a data de fim do leilão (yyyy-MM-dd) ou pressione ENTER para não definir: ");
        String dataFimStr = Tools.scanner.next();
        LocalDate dataFim = dataFimStr.isEmpty() ? null : Tools.parseDate(dataFimStr);

        System.out.println("Insira o valor mínimo:");
        double valorMin = Tools.scanner.nextDouble();

        System.out.println("Insira o valor máximo (ou -1 se não quiser definir): ");
        double valorMax = Tools.scanner.nextDouble();

        double multiploLance = 0;
        if (tipoLeilao == 1) {
            System.out.println("Insira o múltiplo de lance:");
            multiploLance = Tools.scanner.nextDouble();
        }

        // Chamada ao método criarLeiloes()
        boolean criado = LeilosController.criarLeiloes(produto, descricao, tipoLeilao, dataInicio, dataFim, valorMax, valorMin, multiploLance);

        if (criado) {
            System.out.println("Leilão criado com sucesso!");
        } else {
            System.out.println("Erro ao criar leilão. Verifique os dados inseridos.");
        }
    }
}
