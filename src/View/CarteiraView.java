package View;

import Model.Carteira;
import Model.ClienteSessao;
import Utils.Tools;

public class CarteiraView {
    public static void exibirMenuCarteira() {
        int opc;
        do {
            System.out.println("\n" + "=".repeat(8) + " CARTEIRA " + "=".repeat(8));
            System.out.println("1. Adicionar Créditos");
            System.out.println("2. Ver Depósitos");
            System.out.println("3. Ver Carteira");
            System.out.println("4. Ver Transações");
            System.out.println("0. Voltar ao menu principal...");
            System.out.print("Escolha uma opção: ");
            opc = Tools.scanner.nextInt();
            switch (opc) {
                case 1:
                    System.out.println("Funcionalidade em desenvolvimento");
                    adicionarCreditos();
                    break;
                case 2:
                    System.out.println("Funcionalidade em desenvolvimento");
                    //verDepositos();
                    break;
                case 3:
                    System.out.println("Funcionalidade em desenvolvimento");
                    //verCarteira();
                    break;
                case 4:
                    System.out.println("Funcionalidade em desenvolvimento");
                    //verTransacoes();
                    break;
                case 0:
                    System.out.println("\nVoltando ao menu anterior...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente...");
            }
        } while (opc != 0);
    }

    private static void adicionarCreditos() {
        int idCliente = ClienteSessao.getIdCliente();
        System.out.println("\nADICIONAR CRÉDITOS");
        System.out.println("Saldo atual: ");
    }

    //private static Carteira buscarInformacoes(int idCliente) {    }
}
