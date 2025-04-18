package View;

import Controller.ProdutoController;
import Model.Produto;
import Model.ResultadoOperacao;
import Utils.Constantes;
import Utils.Tools;

import java.util.List;

public class ProdutoView {
    private final ProdutoController produtoController;

    public ProdutoView(ProdutoController produtoController) {
        this.produtoController = produtoController;
    }

    public void exibirProduto() {
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
                    listarProduto(false);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente...");
            }
        } while (opcao != 0);
    }

    public void criarProduto() {
        System.out.println("\nCRIAÇÃO DE UM PRODUTO\n");

        System.out.println("Insira o nome do produto " + Tools.alertaCancelar());
        String nome = Tools.scanner.nextLine();
        if (Tools.verificarSaida(nome)) return;

        System.out.println("Insira uma descrição do produto " + Tools.alertaCancelar());
        String descricao = Tools.scanner.nextLine();
        if (Tools.verificarSaida(descricao)) return;

        ResultadoOperacao resultado = produtoController.criarProduto(0, Constantes.estadosProduto.ATIVO, nome, descricao);
        if (resultado.Sucesso) {
            System.out.println("Produto criado com sucesso!");
        } else {
            System.out.println(resultado.msgErro);
        }
    }

    public void editarProduto() {
        listarProduto(false);

        System.out.println("\nEDIÇÃO DE UM PRODUTO");
        System.out.println("Insira o ID do produto que deseja editar " + Tools.alertaCancelar());
        int id = Tools.scanner.nextInt();
        Tools.scanner.nextLine();
        if (Tools.verificarSaida(String.valueOf(id))) return;

        Produto produto = produtoController.procurarProduto(id);
        if (produto != null) {
            exibirDetalhesProduto(produto);

            System.out.print("\nNovo nome do produto ou pressione ENTER para não alterar " + Tools.alertaCancelar());
            String nome = Tools.scanner.nextLine().trim();
            if (Tools.verificarSaida(nome)) return;
            if (nome.isEmpty()) nome = produto.getNome();

            System.out.print("Nova descrição do produto ou pressione ENTER para não alterar " + Tools.alertaCancelar());
            String descricao = Tools.scanner.nextLine().trim();
            if (Tools.verificarSaida(descricao)) return;
            if (descricao.isEmpty()) descricao = produto.getDescricao();

            int idEstado = produto.getEstado();
            if (produtoController.verificarProdutoEmLeilao(id)) {
                System.out.println("O produto já se encontra associado a um leilão, deste modo não será possível alterar o estado.");
            } else {
                while (true) {
                    System.out.print("Novo estado do produto ou pressione ENTER para não alterar " + Tools.alertaCancelar());
                    String estadoStr = Tools.scanner.nextLine().trim();
                    if (Tools.verificarSaida(estadoStr)) return;
                    if (!estadoStr.isEmpty()) {
                        try {
                            idEstado = Integer.parseInt(estadoStr);
                            if (idEstado != Constantes.estadosProduto.ATIVO && idEstado != Constantes.estadosProduto.INATIVO) {
                                System.out.println("Opção inválida. Tente novamente...");
                                idEstado = produto.getEstado();
                                continue;
                            }
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Entrada inválida. Insira um número entre 0 e 3.");
                        }
                    }
                }
            }
            boolean sucesso = produtoController.editarProduto(id, nome, descricao, idEstado);
            if (sucesso) {
                System.out.println("Produto editado com sucesso!");
            } else {
                System.out.println("Não foi possível editar o produto.");
            }
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    public void eliminarProduto() {
        listarProduto(false);

        System.out.println("\nELIMINAÇÃO DE UM PRODUTO");
        System.out.println("Insira o ID do produto que deseja eliminar " + Tools.alertaCancelar());
        int id = Tools.scanner.nextInt();
        Tools.scanner.nextLine();
        if (Tools.verificarSaida(String.valueOf(id))) return;
        Produto produto = produtoController.procurarProduto(id);

        if (produto != null) {
            if (produto.getEstado() == Constantes.estadosProduto.RESERVADO) {
                System.out.println("O produto está reservado e não pode ser eliminado.");
                return;
            }
            exibirDetalhesProduto(produto);
            System.out.println("\nTem a certeza que pretende eliminar o produto com o Id " + id + "? (S/N)");
            String opcao = Tools.scanner.nextLine().trim().toUpperCase();

            switch (opcao) {
                case "S":
                    boolean sucesso = produtoController.eliminarProduto(produto);
                    if (sucesso) System.out.println("Produto eliminado com sucesso!");
                    else System.out.println("Não foi possível eliminar o produto.");
                    break;
                case "N":
                    System.out.println("A eliminação do produto foi cancelada.");
                    break;
                default:
                    System.out.println("Opção Inválida. Tente novamente...");
            }
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    public void listarProduto(boolean apenasDisponiveis) {
        ResultadoOperacao resultado = produtoController.listarProduto(apenasDisponiveis);
        if (resultado.Sucesso) {
            List<Produto> produtos = (List<Produto>) resultado.Objeto;
            exibirProduto(produtos);
        } else {
            System.out.println(resultado.msgErro);
        }
    }

    public void exibirProduto(List<Produto> produtos) {
        if (!produtos.isEmpty()) {
            System.out.println("\n" + "=".repeat(5) + " LISTAGEM DOS PRODUTOS " + "=".repeat(5));
            System.out.printf("%-8s %-20s %-30s %-40s\n",
                    "Id", "Estado", "Nome", "Descrição");
            System.out.println("-".repeat(95));
            for (Produto produto : produtos) {
                String nomeEstado = Tools.estadoProduto.fromCodigo(produto.getEstado()).name();
                System.out.printf("%-8s %-20s %-30s %-40s\n",
                        produto.getIdProduto(),
                        nomeEstado,
                        produto.getNome().toUpperCase(),
                        produto.getDescricao().toUpperCase());
            }
        } else {
            System.out.println("Não existem produtos disponíveis para leilão.");
        }
    }

    public void exibirDetalhesProduto(Produto produto) {
        System.out.println("\nDETALHES DO LEILÃO COM O ID " + produto.getIdProduto());
        System.out.println("Estado: " + produto.getEstado());
        System.out.println("Nome: " + produto.getNome());
        System.out.println("Descrição: " + produto.getDescricao());
    }
}
