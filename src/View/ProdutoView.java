package View;

import Controller.ProdutoController;
import Model.Produto;
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

        ProdutoController.criarProduto(0,nome, descricao);

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

        Produto produto = ProdutoController.procurarProduto(id);
        if(produto != null) {

            System.out.println("\nTêm a certeza que quer eliminar este produto ?");
            System.out.println("\nInsira (S) para eliminar ou (N) para recusar o produto: ");
            char opcao =  Character.toUpperCase(Tools.scanner.next().charAt(0));
            switch (opcao) {
                case 's':
                    boolean sucesso = ProdutoController.eliminarProduto(produto);
                    if (sucesso) {
                        System.out.println("Produto eliminado com sucesso.");
                    }else{
                        System.out.println("Não foi possivel eliminar o produto.");
                    }
                    break;
                case 'n':
                    System.out.println("A eliminação do produto foi cancelada.");
                    break;
                default:
                    System.out.println("A opção foi invalida. Tente novamente.");
            }
        }else{
            System.out.println("[(ERRO)}");
        }



    }

    public static void listarProduto() {
        System.out.println("\n --- LISTA DE PRODUTOS ---");
        ProdutoController.listarProduto();
    }

    public static void exibirDetalhesProduto(Produto produto) {
        System.out.println("\nDETALHES DO LEILÃO COM O ID " + produto.getIdProduto());
        System.out.println("Nome: " + produto.getNome());
        System.out.println("Descrição: " + produto.getDescricao());
    }
}
