package View;

import Controller.CategoriaController;
import Controller.LeilaoController;
import Model.Categoria;
import Model.Leilao;
import Model.ResultadoOperacao;
import Utils.Constantes;
import Utils.Tools;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoriaView {

    public void exibirMenuCategoria() throws MessagingException, IOException {
        int opc;
        do {
            System.out.println("\n" + "=".repeat(5) + " MENU CATEGORIA " + "=".repeat(5));
            System.out.println("1. Criar Categoria");
            System.out.println("2. Editar Categoria");
            System.out.println("3. Eliminar Categoria");
            System.out.println("4. Listar Categorias");
            System.out.println("0. Voltar ao menu principal...");
            opc = Tools.pedirOpcaoMenu("Escolha uma opção: ");
            Tools.scanner.nextLine();
            switch (opc) {
                case 1:
                    criarCategoria();
                    break;
                case 2:
                    editarCategoria();
                    break;
                case 3:
                    eliminarCategoria();
                    break;
                case 4:
                    listarCategoria();
                    break;
                case 0:
                    System.out.println("\nSair...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente...");
            }
        } while (opc != 0);
    }

    public void criarCategoria() throws MessagingException, IOException{
        CategoriaController categoriaController = new CategoriaController();
        System.out.println("\nCRIAÇÃO DE UMA CATEGORIA\n");

        System.out.println("Insira o descrição da categoria " + Tools.alertaCancelar());
        String descricao = Tools.scanner.nextLine();
        if (Tools.verificarSaida(descricao)) return;

        ResultadoOperacao resultado = categoriaController.criarCategoria(0, descricao, Constantes.estadosCategoria.INATIVO);
        if (resultado.Sucesso) {
            System.out.println("Categoria criada com sucesso!");
        } else {
            System.out.println(resultado.msgErro);
        }
    }

    public void listarCategoria() throws MessagingException, IOException {
        CategoriaController categoriaController = new CategoriaController();
        List<Categoria> categorias = categoriaController.listarCategoria();
        if (!categorias.isEmpty()) {
            exibirCategoria(categorias);
        } else {
            System.out.println("Não existem categorias.");
        }
    }

    public void exibirCategoria(List<Categoria> categorias) {
        if (!categorias.isEmpty()) {
            System.out.println("\n" + "=".repeat(5) + " LISTAGEM DAS CATEGORIAS " + "=".repeat(5));
            System.out.printf("%-8s %-20s %-20s\n", "Id", "Descrição","Estado");
            System.out.println("-".repeat(50));
            for (Categoria categoria : categorias) {
                String nomeEstado = Tools.estadoCategoria.fromCodigo(categoria.getEstado()).name();
                System.out.printf("%-8d %-20s %-20s\n",
                        categoria.getIdCategoria(),
                        categoria.getDescricao().toUpperCase(),
                        nomeEstado);
            }
        } else {
            System.out.println("Não existem categorias disponíveis.");
        }
    }

    public void editarCategoria() throws MessagingException, IOException {
        CategoriaController categoriaController = new CategoriaController();
        List<Categoria> categorias = categoriaController.listarCategoria();

        if (categorias.isEmpty()) {
            System.out.println("Não existem categorias para editar.");
            return;
        }

        exibirCategoria(categorias);

        System.out.println("\nEDIÇÃO DE UMA CATEGORIA");
        int id = Tools.pedirOpcaoMenu("Insira o ID da categoria que deseja editar " + Tools.alertaCancelar());
        Tools.scanner.nextLine();
        if (Tools.verificarSaida(String.valueOf(id))) return;

        Categoria categoria = categoriaController.procurarCategoria(id);
        if (categoria != null) {
            exibirDetalhesCategoria(categoria);

            System.out.print("Nova descrição da categoria " + Tools.alertaCancelar());
            String descricao = Tools.scanner.nextLine();
            if (Tools.verificarSaida(descricao)) return;
            if (descricao.isEmpty()) descricao = categoria.getDescricao();

            boolean sucesso = categoriaController.editarCategoria(id, descricao);
            if (sucesso) {
                System.out.println("Categoria editada com sucesso!");
            } else {
                System.out.println("Não foi possível editar a categoria.");
            }
        } else {
            System.out.println("Categoria não encontrada.");
        }
    }

    public void eliminarCategoria() throws MessagingException, IOException {
        CategoriaController categoriaController = new CategoriaController();
        listarCategoria();

        System.out.println("\nELIMINAÇÃO DE UMA CATEGORIA");
        int id = Tools.pedirOpcaoMenu("Insira o ID da categoria que deseja eliminar " + Tools.alertaCancelar());
        Tools.scanner.nextLine();

        if (Tools.verificarSaida(String.valueOf(id))) return;
        Categoria categoria = categoriaController.procurarCategoria(id);

        if (categoria != null) {
            if (categoria.getEstado() == 1) {
                System.out.println("Não é permitido eliminar uma categoria com estado ativo.");
                return;
            }

            exibirDetalhesCategoria(categoria);
            System.out.println("\nTem a certeza que pretende eliminar a categoria com o Id " + id + "? (S/N)");
            String opcao = Tools.scanner.nextLine().trim().toUpperCase();

            switch (opcao) {
                case "S":
                    boolean sucesso = categoriaController.eliminarCategoria(categoria);
                    if (sucesso) System.out.println("Categoria eliminada com sucesso!");
                    else System.out.println("Não foi possível eliminar a categoria.");
                    break;
                case "N":
                    System.out.println("A eliminação da categoria foi cancelada.");
                    break;
                default:
                    System.out.println("Opção Inválida. Tente novamente...");
            }
        } else {
            System.out.println("Categoria não encontrada.");
        }
    }


    public void exibirDetalhesCategoria(Categoria categoria) {
        String nomeEstado = Tools.estadoCategoria.fromCodigo(categoria.getEstado()).name();
        System.out.println("\nDETALHES DA CATEGORIA " + categoria.getIdCategoria());
        System.out.println("Descrição: " + categoria.getDescricao());
        System.out.println("Estado: " + nomeEstado);
    }

    public List<Leilao> filtrarLeiloesPorCategoria() throws MessagingException, IOException {
        CategoriaController categoriaController = new CategoriaController();
        List<Leilao> leiloesFiltrados = new ArrayList<>();

        while (true) {
            System.out.print("Insira a descrição da categoria para filtrar " + Tools.alertaCancelar() + " :");
            String descricaoCategoria = Tools.scanner.nextLine().trim();

            leiloesFiltrados = categoriaController.filtrarLeiloesPorCategoria(descricaoCategoria);

            if (leiloesFiltrados.isEmpty()) {
                System.out.println("Não foram encontrados leilões para a categoria especificada. Tente novamente.");
            } else {
                return leiloesFiltrados;
            }
        }
    }


    public void exibirLeiloesPorCategoria(List<Leilao> leiloesFiltrados) {
        if (!leiloesFiltrados.isEmpty()) {
            System.out.println("\n===== LEILÕES FILTRADOS =====");
            for (Leilao leilao : leiloesFiltrados) {
                System.out.println("ID: " + leilao.getId() + " | Descrição do Produto: " + leilao.getDescricao());
            }
        } else {
            System.out.println("Nenhum leilão encontrado para a categoria especificada.");
        }
    }




}
