package View;

import Model.Utilizador;
import Utils.Tools;

import static Utils.Tools.scanner;

public class MenuClienteView {
    public void exibirMenu(Utilizador utilizador) {
        //
        UtilizadorView utilizadorView = new UtilizadorView();
        TransacaoView transacaoView = new TransacaoView();
        LanceView lanceView = new LanceView();

        while (true) {
            System.out.println("\n" + "=".repeat(5) + " MENU CLIENTE " + "=".repeat(5));
            System.out.println("1. Ver os Meus Dados");
            System.out.println("2. Editar Ficha de Cliente");
            System.out.println("3. Carteira");
            System.out.println("4. Menu Lances");
            System.out.println("0. Sair...");

            int opcao = Tools.pedirOpcaoMenu("Escolha uma opção: ");
            scanner.nextLine().trim();

            switch (opcao) {
                case 1:
                    utilizadorView.verDadosCliente(utilizador);
                    break;
                case 2:
                    utilizadorView.editarCliente(utilizador);
                    break;
                case 3:
                    transacaoView.exibirMenuTransacao();
                    break;
                case 4:
                    lanceView.exibirMenuLance();
                    break;
                case 0:
                    System.out.println("A sair...");
                    Tools.clienteSessao.logout();
                    return;
                default:
                    System.out.println("Opção inválida, tenta novamente.");
            }
        }
    }
}
