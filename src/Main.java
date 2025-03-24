import java.util.Scanner;

import View.ImportView;
import View.ProdutoView;
import View.LeilaoView;
import View.LoginView;
import View.RegistarClienteView;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n"+"=".repeat(5) + " LEILOEIRA " + "=".repeat(5));
            System.out.println("1. Registar Utilizador");
            System.out.println("2. Login Cliente");
            System.out.println("3. Listagem de Utilizadores");
            System.out.println("4. Menu Leilões");
            System.out.println("5. Menu Produtos");
            System.out.println("0. Sair...");
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
                    ImportView.mostrarUtilizador();
                    break;
                case 4:
                    LeilaoView.exibirMenuLeiloes();
                    break;
                case 5:
                    ProdutoView.exibirProduto();
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
