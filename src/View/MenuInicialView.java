package View;

import BLL.LanceBLL;


import static Utils.Tools.scanner;

public class MenuInicialView {
    public static void menuInicial() {
        int opcao = -1;
        do {
            System.out.println("\nBem-vindo à leiloeira Valor em Alta!\n");

            System.out.println("1. Efetuar Login");
            System.out.println("2. Efetuar Registo");
            System.out.println("3. Testes");
            System.out.println("0. Sair...");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    int tipoUtilizador = LoginView.login();
                    if (tipoUtilizador == -1) {
                        continue;
                    } else if (tipoUtilizador == 1) {
                        MenuGestorView.exibirMenu();
                    } else if (tipoUtilizador == 2) {
                        MenuClienteView.exibirMenu();
                    } else {
                        System.out.println("Tipo de utilizador inválido.");
                    }
                    break;
                case 2:
                    RegistarClienteView.MenuRegistarCliente();
                    break;
                case 3:
                    LanceView.exibirMenuLance();
                case 0:
                    System.out.println("A desligar sistema...");
                    return;
                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        } while (opcao != 0);
    }
}
