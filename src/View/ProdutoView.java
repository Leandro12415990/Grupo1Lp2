package View;

import Controller.ProdutoController;
import Model.Produto;
import Utils.Tools;

import java.util.List;


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

            System.out.println("Insira o nome do produto (-1 para cancelar): ");
            String nome = Tools.scanner.nextLine();
            if (nome.equals("-1")) {
                System.out.println("Voltando ao menu anterior...");
                return;
            }

            System.out.println("Insira uma descrição do produto (-1 para cancelar): ");
            String descricao = Tools.scanner.nextLine();
            if (descricao.equals("-1")) {
                System.out.println("Voltando ao menu anterior...");
                return;
            }
            ProdutoController.criarProduto(0, Tools.estadoProduto.ATIVO.getCodigo(), nome, descricao);
    }

    public static void editarProduto() {

        listarProduto();

        System.out.println("\n --- EDITAR PRODUTO ---");

        System.out.println("Insira o ID do produto que deseja editar: (-1 para cancelar)");
        int id = Tools.scanner.nextInt();
        Tools.scanner.nextLine();
        if (id == -1) {
            System.out.println("Voltando ao menu anterior...");
            return;
        }


        Produto produto = ProdutoController.procurarProduto(id);
        if(produto != null){
            exibirDetalhesProduto(produto);
            boolean sucesso = ProdutoController.editarProduto(produto);
            if(sucesso) {
                System.out.println("Produto editado com sucesso!");
            }
            else{
                System.out.println("Não foi possivel editar o produto.");
            }
        }else{
            System.out.println("[ERRO] Não foi possível encontrar o produto com o ID fornecido.");
        }
    }

    public static void eliminarProduto() {

        listarProduto();

        System.out.println("\n --- ELIMINAR PRODUTO ---");
        System.out.println("Insira o ID do produto que deseja eliminar: (-1 para cancelar)");
        int id = Tools.scanner.nextInt();
        Tools.scanner.nextLine();
        if (id == -1) {
            System.out.println("Voltando ao menu anterior...");
            return;
        }

        Produto produto = ProdutoController.procurarProduto(id);

        if (produto != null) {
            if (produto.getEstado() == Tools.estadoProduto.RESERVADO.getCodigo()) {
                System.out.println("Este produto está reservado e não pode ser eliminado.");
                return;
            }
            exibirDetalhesProduto(produto);
            System.out.println("\nTem a certeza que quer eliminar este produto?");
            System.out.println("\nInsira (S) para eliminar ou (N) para recusar o produto: ");

            String opcao = Tools.scanner.nextLine().trim().toUpperCase();

            switch (opcao) {
                case "S":
                    boolean sucesso = ProdutoController.eliminarProduto(produto);
                    if (sucesso) {
                        System.out.println("Produto eliminado com sucesso!");
                    } else {
                        System.out.println("Não foi possível eliminar o produto.");
                    }
                    break;
                case "N":
                    System.out.println("A eliminação do produto foi cancelada.");
                    break;
                default:
                    System.out.println("A opção foi inválida. Tente novamente.");
            }
        } else {
            System.out.println("[ERRO] Não foi possível encontrar o produto com o ID fornecido.");
        }
    }

    public static void listarProduto() {
        ProdutoController.listarProduto();
    }
    public static void exibirProduto(List<Produto> produtos) {
        System.out.println("\n" + "=".repeat(5) + " LISTAGEM DOS PRODUTOS " + "=".repeat(5));
        System.out.printf("%-8s %-8s %-30s %-40s\n",
                "Id", "Estado", "Nome", "Descrição");
        System.out.println("-".repeat(260));


        for (Produto produto : produtos) {

            String nomeEstado = Tools.estadoProduto.fromCodigo(produto.getEstado()).name();
            System.out.printf("%-8s %-8s %-30s %-40s\n",
                    produto.getIdProduto(),
                    nomeEstado,
                    produto.getNome(),
                    produto.getDescricao());
        }
    }

    

    public static void exibirDetalhesProduto(Produto produto) {
        System.out.println("\nDETALHES DO LEILÃO COM O ID " + produto.getIdProduto());
        System.out.println("Estado: " + produto.getEstado());
        System.out.println("Nome: " + produto.getNome());
        System.out.println("Descrição: " + produto.getDescricao());
    }
}
