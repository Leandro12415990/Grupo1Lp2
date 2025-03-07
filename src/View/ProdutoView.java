package View;

import Controller.ProdutoController;
import Utils.Tools;

import java.util.ArrayList;

public class ProdutoView {

    public static void exibirProduto() {
        int opcao;

        do {
            System.out.println("\nMenu:");
            System.out.println("1. Criar Produto");
            System.out.println("2. Editar Produto");
            System.out.println("3. Apagar Produto");
            System.out.println("4. Listar Produtos");
            System.out.println("0. Voltar ao menu principal...");
            System.out.print("Escolha uma opção: ");
            opcao = Tools.scanner.nextInt();
            Tools.scanner.nextLine();

            switch (opcao) {
                case 1:
                    criarProduto();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    listarProduto();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente...");
            }
        } while (opcao != 0);
    }

    public static void criarProduto() {
        System.out.println("Insira o nome do produto: ");
        String nome = Tools.scanner.nextLine();
        System.out.println("Insira uma descrição do produto: ");
        String descricao = Tools.scanner.nextLine();

        ProdutoController.criarProduto(nome, descricao);
    }

    public static void listarProduto() {
        ProdutoController.listarProduto();
    }

}
