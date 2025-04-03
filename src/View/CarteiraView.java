package View;

import Controller.CarteiraController;
import Model.ClienteSessao;
import Model.ResultadoOperacao;
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

        Double saldoAtual = buscarValorTotalAtual(idCliente);
        Double saldoPendente = valorPendente(idCliente);
        System.out.println("Saldo atual: " + saldoAtual + "€");
        System.out.println("Saldo pendente de aprovação: " + saldoPendente + "€");

        System.out.print("Insira a quantidade de créditos que pretende depositar " + Tools.alertaCancelar());
        Double creditos = Tools.scanner.nextDouble();
        if(Tools.verificarSaida(String.valueOf(creditos))) return;

        ResultadoOperacao resultado = CarteiraController.criarDeposito(idCliente, saldoAtual, creditos);
        if (resultado.Sucesso) System.out.println("Os créditos serão adicionados à sua conta quando o gestor aprovar o depósito!");
            else System.out.println(resultado.msgErro);
    }

    private static Double buscarValorTotalAtual(int idCliente) {
        return CarteiraController.buscarValorTotalAtual(idCliente);
    }

    private static Double valorPendente(int idCliente) {
        return CarteiraController.valorPendente(idCliente);
    }
}
