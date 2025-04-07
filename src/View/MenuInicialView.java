package View;

import Model.Utilizador;
import Utils.Tools;
import BLL.LanceBLL;



import static Utils.Tools.scanner;

public class MenuInicialView {
    public static void menuInicial() {
        int opcao;
        do {
            System.out.println("\nBem-vindo à leiloeira Valor em Alta!\n");

            System.out.println("1. Efetuar Login");
            System.out.println("2. Efetuar Registo");
            System.out.println("0. Sair...");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine().trim();

            switch (opcao) {
                case 1:
                    Utilizador utilizador = LoginView.login();
                    if (utilizador == null) {
                        System.out.println("Erro a fazer Login");
                    } else if (utilizador.getTipoUtilizador() == Tools.tipoUtilizador.GESTOR.getCodigo()) {
                        MenuGestorView.exibirMenu();
                    } else if (utilizador.getTipoUtilizador() == Tools.tipoUtilizador.CLIENTE.getCodigo()) {
                        MenuClienteView.exibirMenu(utilizador);
                    }
                    break;
                case 2:
                    RegistarClienteView.MenuRegistarCliente();
                    break;
                case 0:
                    System.out.println("A desligar sistema...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        } while (opcao != 0);
    }
}
