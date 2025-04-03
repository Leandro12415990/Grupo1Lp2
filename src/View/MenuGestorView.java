package View;

import Utils.Tools;

import static Utils.Tools.scanner;

public class MenuGestorView {
    public static void exibirMenu() {
        while (true) {
            System.out.println("\n"+"=".repeat(5) + " MENU GESTOR DA LEILOEIRA " + "=".repeat(5));
            System.out.println("1. Listagem de Utilizadores");
            System.out.println("2. Aprovar Clientes");
            System.out.println("3. Menu Leilões");
            System.out.println("4. Menu Produtos");
            System.out.println("5. Listagens");
            System.out.println("0. Sair...");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine().trim();
            switch (opcao) {
                case 1:
                    ImportView.mostrarUtilizador(Tools.estadoUtilizador.getDefault().getCodigo(), 2);
                    break;
                case 2:
                    AprovarClienteView.exibirMenu();
                    break;
                case 3:
                    LeilaoView.exibirMenuLeiloes();
                    break;
                case 4:
                    ProdutoView.exibirProduto();
                    break;
                case 5:
                    EstatisticaView.exibirMenuListagem();
                    break;
                case 0:
                    System.out.println("A desligar sistema...");
                    return;
                default:
                    System.out.println("Opção inválida, tenta novamente.");
            }
        }
    }
}
