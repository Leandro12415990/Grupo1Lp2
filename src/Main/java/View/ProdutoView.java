package View;

import Controller.CategoriaController;
import Controller.ProdutoCategoriaController;
import Controller.ProdutoController;
import Model.Categoria;
import Model.Produto;
import Model.ProdutoCategoria;
import Model.ResultadoOperacao;
import Utils.Constantes;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.List;

public class ProdutoView {
    public void exibirProduto() throws MessagingException, IOException {
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

    public void criarProduto() throws MessagingException, IOException {
        ProdutoCategoriaController produtoCategoriaController = new ProdutoCategoriaController();
        ProdutoController produtoController = new ProdutoController();
        CategoriaView categoriaView = new CategoriaView();

        System.out.println("\nCRIAÇÃO DE UM PRODUTO\n");

        System.out.println("Insira o nome do produto " + Tools.alertaCancelar());
        String nome = Tools.scanner.nextLine();
        if (Tools.verificarSaida(nome)) return;

        System.out.println("Insira uma descrição do produto " + Tools.alertaCancelar());
        String descricao = Tools.scanner.nextLine();
        if (Tools.verificarSaida(descricao)) return;

        categoriaView.listarCategoria();
        System.out.println("Insira o ID da categoria para associar ao produto " + Tools.alertaCancelar());
        int idCategoria = Tools.scanner.nextInt();
        Tools.scanner.nextLine();
        if (Tools.verificarSaida(String.valueOf(idCategoria))) return;

        ResultadoOperacao resultado = produtoController.criarProduto(0, Constantes.estadosProduto.ATIVO, nome, descricao);

        if (resultado.Sucesso) {
            Produto produtoCriado = (Produto) resultado.Objeto;
            int idProdutoCriado = produtoCriado.getIdProduto();

            boolean associou = produtoCategoriaController.associarCategoriaAoProduto(idProdutoCriado, idCategoria);

            if (associou) {
                System.out.println("Produto criado e categoria associada com sucesso!");
            } else {
                System.out.println("Produto criado mas falhou a associação da categoria.");
            }
        } else {
            System.out.println(resultado.msgErro);
        }
    }

    public void editarProduto() throws MessagingException, IOException {
        CategoriaController categoriaController = new CategoriaController();
        ProdutoController produtoController = new ProdutoController();
        ProdutoCategoriaController produtoCategoriaController = new ProdutoCategoriaController();
        CategoriaView categoriaView = new CategoriaView();
        listarProduto(false);

        System.out.println("\nEDIÇÃO DE UM PRODUTO");
        listarProduto(true);
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
                            System.out.println("Entrada inválida. Insira um número válido.");
                        }
                    } else {
                        break;
                    }
                }
            }

            System.out.print("Quer alterar a categoria do produto? (S/N): ");
            String alterarCategoria = Tools.scanner.nextLine().trim().toUpperCase();
            int idCategoriaNova = -1;
            if (alterarCategoria.equals("S")) {

                categoriaView.listarCategoria();
                System.out.print("Insira o ID da nova categoria " + Tools.alertaCancelar());
                idCategoriaNova = Tools.scanner.nextInt();
                Tools.scanner.nextLine();
                if (Tools.verificarSaida(String.valueOf(idCategoriaNova))) return;
            }

            boolean sucesso = produtoController.editarProduto(id, nome, descricao, idEstado);

            if (sucesso) {
                System.out.println("Produto editado com sucesso!");

                if (idCategoriaNova != -1) {
                    boolean desassociou = produtoCategoriaController.desassociarCategoriaDoProduto(id);
                    if (desassociou) {
                        System.out.println("Categoria anterior desassociada com sucesso.");
                    } else {
                        System.out.println("Falha ao desassociar a categoria anterior.");
                    }

                    boolean associou = produtoCategoriaController.associarCategoriaAoProduto(id, idCategoriaNova);
                    if (associou) {
                        System.out.println("Categoria atualizada com sucesso!");
                    } else {
                        System.out.println("Falha ao atualizar categoria.");
                    }

                    if (produtoCategoriaController.verificarCategoriaSemProdutoAssociado(idCategoriaNova)) {
                        boolean estadoAlterado = categoriaController.atualizarCategoriaEstado(idCategoriaNova, Constantes.estadosCategoria.INATIVO);
                        if (estadoAlterado) {
                            System.out.println("Categoria anterior desativada com sucesso.");
                        } else {
                            System.out.println("Falha ao desativar a categoria anterior.");
                        }
                    }
                }
            } else {
                System.out.println("Não foi possível editar o produto.");
            }
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    public void eliminarProduto() throws MessagingException, IOException {
        ProdutoController produtoController = new ProdutoController();
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

    public void listarProduto(boolean apenasDisponiveis) throws MessagingException, IOException {
        ProdutoController produtoController = new ProdutoController();
        List<Produto> produtos = produtoController.listarProduto(apenasDisponiveis);

        if (!produtos.isEmpty()) {
            exibirProduto(produtos);
        } else {
            System.out.println("Não existem produtos disponíveis para leilão.");
        }
    }

    public void exibirProduto(List<Produto> produtos) throws MessagingException, IOException {
        ProdutoCategoriaController produtoCategoriaController = new ProdutoCategoriaController();
        CategoriaController categoriaController = new CategoriaController();

        if (!produtos.isEmpty()) {
            System.out.println("\n" + "=".repeat(5) + " LISTAGEM DOS PRODUTOS " + "=".repeat(5));
            System.out.printf("%-8s %-20s %-30s %-40s %-20s\n",
                    "Id", "Estado", "Nome", "Descrição", "Categoria");
            System.out.println("-".repeat(120));

            for (Produto produto : produtos) {
                ProdutoCategoria produtoCategoria = produtoCategoriaController.procurarCategoriaPorProduto(produto.getIdProduto());

                String categoriaDescricao = "Sem Categoria";
                if (produtoCategoria != null) {
                    Categoria categoria = categoriaController.procurarCategoria(produtoCategoria.getIdCategoria());
                    if (categoria != null) {
                        categoriaDescricao = categoria.getDescricao();
                    }
                }

                String nomeEstado = Tools.estadoProduto.fromCodigo(produto.getEstado()).name();
                System.out.printf("%-8s %-20s %-30s %-40s %-20s\n",
                        produto.getIdProduto(),
                        nomeEstado,
                        produto.getNome().toUpperCase(),
                        produto.getDescricao().toUpperCase(),
                        categoriaDescricao);
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
