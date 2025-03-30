package View;

import Controller.AprovarClienteController;

import static Utils.Tools.scanner;

public class AprovarClienteView {
    public static void exibirMenu() {
        while (true) {
            System.out.println("Menu Aprovar Clientes");
            ImportView.mostrarUtilizador();
            System.out.println("-----------------------------------------------------------------");
            System.out.println("1. Aprovar todos");
            System.out.println("2. Aprovar Cliente especifico");
            System.out.println("0. Sair...");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();
            switch (opcao) {
                case 1:
                    boolean respAprovarTodos = AprovarClienteController.AprovarTodos();
                    if (respAprovarTodos) System.out.println("Utilizadores aprovados com sucesso!");
                    else System.out.println("Utilizadores não foram todos aprovados com sucesso," +
                            "liste os utilizadores no menu para verificar quais não foram aprovados!");
                    break;
                case 2:
                    //boolean respArpovarCliente =
                    break;
                case 0:
                    System.out.println("A desligar sistema...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        }
    }
}
