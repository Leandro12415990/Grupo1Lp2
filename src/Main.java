import java.util.Scanner;

import View.*;

import View.ImportView;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=== MENU ===");
            System.out.println("1. Registar Utilizador");
            System.out.println("2. Login Cliente");
            System.out.println("3. Mostrar Leilões");
            System.out.println("4. Mostrar Utilizadores");
            System.out.println("5. Mostrar Produtos");
            System.out.println("6. Gerir Lances");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();
            switch (opcao) {
                case 1:
                    RegistarClienteView.MenuRegistarCliente();
                    break;
                case 2:
                    LoginView.login();
                    break;
                case 3:
                    ImportView.mostrarLeilao();
                    break;
                case 4:
                    ImportView.mostrarUtilizador();
                    break;
                case 5:
                    ProdutoView.exibirProduto();
                    break;
                case 6:
                    break;
                case 0:
                    System.out.println("A sair...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida, tenta novamente.");
            }
        }
    }
}
