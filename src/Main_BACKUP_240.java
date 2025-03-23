import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import View.ImportView;
<<<<<<< HEAD
import View.LeilaoView;
=======
import View.ProdutoView;

import Model.Utilizador;
import View.ImportView;
import View.LoginView;
import View.RegistarClienteView;
>>>>>>> master

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
<<<<<<< HEAD
            System.out.println("\n"+"=".repeat(5) + " LEILOEIRA " + "=".repeat(5));
            System.out.println("1. Leilões");
            System.out.println("2. Mostrar Utilizadores");
=======
            System.out.println("=== MENU ===");
            System.out.println("1. Registar Utilizador");
            System.out.println("2. Login Cliente");
            System.out.println("3. Mostrar Leilões");
            System.out.println("4. Mostrar Utiliza  dores");
            System.out.println("5. Mostrar Produtos");
>>>>>>> master
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();
            switch (opcao) {
                case 1:
<<<<<<< HEAD
                    LeilaoView.exibirMenuLeiloes();
=======
                    RegistarClienteView.MenuRegistarCliente();
>>>>>>> master
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
