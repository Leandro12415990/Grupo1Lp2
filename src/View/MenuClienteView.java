package View;

import Model.ClienteSessao;
import Model.Utilizador;

import static Utils.Tools.scanner;

public class MenuClienteView {
    public static void exibirMenu(Utilizador utilizador) {
        while (true) {
            System.out.println("\n" + "=".repeat(5) + " MENU CLIENTE " + "=".repeat(5));
            System.out.println("1. Editar Ficha de Cliente");
            System.out.println("2. Carteira");
            System.out.println("3. Menu Lances");
            System.out.println("0. Sair...");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine().trim();
            switch (opcao) {
                case 1:
                    EditarClienteView.exibirMenu(utilizador);
                    break;
                case 2:
                    TransacaoView.exibirMenuTransacao();
                    break;
                case 3:
                    LanceView.exibirMenuLance();
                    break;
                case 0:
                    System.out.println("A sair...");
                    return;
                default:
                    System.out.println("Opção inválida, tenta novamente.");
            }
        }
    }
}
