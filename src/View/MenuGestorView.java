package View;

import static Utils.Tools.scanner;

public class MenuGestorView {
    public static void exibirMenu() {
        while (true) {
            System.out.println("\n"+"=".repeat(5) + " MENU GESTOR DA LEILOEIRA " + "=".repeat(5));
            System.out.println("1. Listagem de Utilizadores");
            System.out.println("2. Menu Leilões");
            System.out.println("3. Menu Produtos");
            System.out.println("0. Sair...");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();
            switch (opcao) {
                case 1:
                    ImportView.mostrarUtilizador();
                    break;
                case 2:
                    LeilaoView.exibirMenuLeiloes();
                    break;
                case 3:
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
