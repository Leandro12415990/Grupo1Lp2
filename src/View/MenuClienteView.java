package View;

import static Utils.Tools.scanner;

public class MenuClienteView {
    public static void exibirMenu() {
        while (true) {
            System.out.println("\n"+"=".repeat(5) + " MENU CLIENTE " + "=".repeat(5));
            System.out.println("1. Editar Ficha de Cliente");
            System.out.println("0. Sair...");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();
            switch (opcao) {
                case 1:
                    System.out.println("Funcionalidade em desenvolvimento...");
                    break;
                case 0:
                    System.out.println("A desligar sistema...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida, tenta novamente.");
            }
        }
    }
}
