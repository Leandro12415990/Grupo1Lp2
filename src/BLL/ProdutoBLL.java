package BLL;

import DAL.ImportDal;
import DAL.ProdutoDal;
import Model.Leilao;
import Model.Produto;
import Utils.Tools;

import java.util.ArrayList;
import java.util.List;

public class ProdutoBLL {
    private static List<Produto> produtos = new ArrayList<>();

    public static List<Produto> carregarProdutos() {
        produtos = ProdutoDal.carregarProdutos();
        return produtos;
    }

    public static void adicionarProduto(Produto produto) {
        carregarProdutos();
        produto.setIdProduto(verificarUltimoId(produtos) + 1);
        produtos.add(produto);
        ProdutoDal.gravarProdutos(produtos);
    }

    public static List<Produto> obterTodosProdutos() {
        return ProdutoDal.carregarProdutos();
    }

    public static List<Produto>  listarProdutos(boolean apenasDisponiveis) {
        carregarProdutos();
        if (!apenasDisponiveis) {
            return produtos;
        }
        List<Produto> produtosAtivos = new ArrayList<>();
        for (Produto produto : produtos) {
            if (produto.getEstado() == 1) {
                produtosAtivos.add(produto);
            }
        }

        return produtosAtivos;
    }

    public static Produto procurarProduto(int id) {
        List<Produto> produtos = obterTodosProdutos();
        for (Produto produto : produtos) {
            if (produto.getIdProduto() == id) {
                return produto;
            }
        }
        return null;

    }

    private static int verificarUltimoId(List<Produto> produtos) {
        int ultimoId = 0;


        for (Produto produto : produtos) {

            if (produto.getIdProduto() > ultimoId) {
                ultimoId = produto.getIdProduto();
            }
        }

        return ultimoId;
    }

    public static boolean editarProduto(Produto produto) {
        List<Produto> produtosAtualizados = ProdutoBLL.obterTodosProdutos();
        boolean produtoEditado = false;

        while (true) {
            System.out.print("Novo estado (1 = ATIVO, 2 = RESERVADO, 3 = INATIVO) - deixe vazio para manter: ");
            String novoEstadoStr = Tools.scanner.nextLine().trim();

            if (novoEstadoStr.isEmpty()) {
                System.out.println("Alteração de estado mantida.");
                break;
            }

            if (novoEstadoStr.equals("1") || novoEstadoStr.equals("2") || novoEstadoStr.equals("3")) {
                int novoEstado = Integer.parseInt(novoEstadoStr);
                produto.setEstado(novoEstado);
                break;
            } else {
                System.out.println(" Estado inválido. Por favor, insira 1, 2 ou 3, ou deixe vazio para manter: .");
            }
        }

        System.out.print("Novo nome (deixe vazio para manter): ");
        String novoNome = Tools.scanner.nextLine().trim();
        if (!novoNome.isEmpty()) {
            produto.setNome(novoNome);
        }
        System.out.print("Nova descrição (deixe vazio para manter): ");
        String novaDescricao = Tools.scanner.nextLine().trim();
        if (!novaDescricao.isEmpty()) {
            produto.setDescricao(novaDescricao);
        }

        for (int i = 0; i < produtosAtualizados.size(); i++) {
            if (produtosAtualizados.get(i).getIdProduto() == produto.getIdProduto()) {
                produtosAtualizados.set(i, produto);
                produtoEditado = true;
                break;
            }
        }

        if (produtoEditado) {
            ProdutoDal.salvarProdutos(produtosAtualizados);
            System.out.println("Produto atualizado e salvo com sucesso.");
        }

        return produtoEditado;
    }

    public static boolean eliminarProduto(Produto produto) {
        List<Produto> produtos = obterTodosProdutos();
        boolean produtoRemovido = false;

        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getIdProduto() == produto.getIdProduto()) {
                produtos.remove(i);
                produtoRemovido = true;
                break;
            }
        }

        if (produtoRemovido) {
            ProdutoDal.salvarProdutos(produtos);
        }

        return produtoRemovido;
    }

    public static String getNomeProdutoById(int idProduto) {
        carregarProdutos();
        for (Produto produto : produtos) {
            if (produto.getIdProduto() == idProduto) {
                return produto.getNome().toUpperCase();
            }
        }
        return null;
    }

    public static boolean verificarDisponibilidadeProduto(int idProduto) {
        List<Produto> produtos = ProdutoDal.carregarProdutos();
        final int EstadoProdutoAtivo = 1;
        for (Produto produto : produtos) {
            if (produto.getIdProduto() == idProduto) {
                if(produto.getEstado() == EstadoProdutoAtivo) return true;
                else return false;
            }
        }
        return false;
    }

    public static void atualizarEstadoProduto(int idProduto, int novoIdEstado) {
        carregarProdutos();
        for (Produto produto : produtos) {
            if (produto.getIdProduto() == idProduto) {
                produto.setEstado(novoIdEstado);
            }
        }
        ProdutoDal.gravarProdutos(produtos);
    }
}
