package View;

import Controller.ProdutoController;
import Utils.Tools;

import java.sql.SQLOutput;

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
                    editarProduto();
                    break;
                case 3:
                    eliminarProduto();
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
        System.out.println("\n --- Criar Produto ---");
        System.out.println("Insira o nome do produto: ");
        String nome = Tools.scanner.nextLine();
        System.out.println("Insira uma descrição do produto: ");
        String descricao = Tools.scanner.nextLine();

        ProdutoController.criarProduto(nome, descricao);

    }

    public static void editarProduto() {
        System.out.println("\n --- EDITAR PRODUTO ---");
        System.out.println("Insira o ID do produto que deseja editar: ");
        int id = Tools.scanner.nextInt();
        Tools.scanner.nextLine();

        ProdutoController.editarProduto(id);
    }

    public static void eliminarProduto() {
        System.out.println("\n --- ELIMINAR PRODUTO ---");
        System.out.println("Insira o ID do produto que deseja eliminar: ");
        int id = Tools.scanner.nextInt();
        Tools.scanner.nextLine();

       ProdutoController.eliminarProduto(id);

    }

    public static void listarProduto() {
        System.out.println("\n --- LISTA DE PRODUTOS ---");
        ProdutoController.listarProduto();
    }

}
