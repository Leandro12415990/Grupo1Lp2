import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Controller.ProdutoController;
import Model.Produto;
import BLL.ProdutoBLL;
import View.ImportView;
import View.ProdutoView;

public class Main {
    public static void main(String[] args) {
        List<Produto> produtos = ProdutoBLL.VerificarIdProdutos();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("=== MENU ===");
            System.out.println("1. Mostrar Leilões");
            System.out.println("2. Mostrar Utilizadores");
            System.out.println("3. Mostrar Utilizadores");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();
            switch (opcao) {
                case 1:
                    ImportView.mostrarLeilao();
                    break;
                case 2:
                    ImportView.mostrarUtilizador();
                    break;
                case 3:
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
