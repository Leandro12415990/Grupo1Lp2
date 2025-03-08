import java.util.Scanner;
import View.ImportView;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=== MENU ===");
            System.out.println("1. Registar Utilizador");
            System.out.println("2. Mostrar Leilões");
            System.out.println("3. Mostrar Utilizadores");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();
            switch (opcao) {
                case 1:
                    break;
                case 2:
                    ImportView.mostrarLeilao();
                    break;
                case 3:
                    ImportView.mostrarUtilizador();
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
