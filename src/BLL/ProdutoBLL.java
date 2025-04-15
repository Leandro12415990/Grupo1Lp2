package BLL;

import DAL.ImportDal;
import DAL.ProdutoDal;
import Model.Leilao;
import Model.Produto;
import Utils.Constantes;
import Utils.Tools;

import java.util.ArrayList;
import java.util.List;

public class ProdutoBLL {
    private static List<Produto> produtos = new ArrayList<>();

    public static List<Produto> carregarProdutos() {
        produtos = ProdutoDal.carregarProdutos();
        int idEstado;
        for (Produto produto : produtos) {
            idEstado = determinarEstadoProduto(produto);
            produto.setEstado(idEstado);
            ProdutoDal.gravarProdutos(produtos);
        }
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

    public static List<Produto> listarProdutos(boolean apenasDisponiveis) {
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

    public static boolean editarProduto(int idProduto, String nome, String descricao, int idEstado) {
        Produto produto = procurarProduto(idProduto);
        if (produto != null) {
            produto.setNome(nome);
            produto.setDescricao(descricao);
            produto.setEstado(idEstado);
            ProdutoDal.gravarProdutos(produtos);
            return true;
        }
        return false;
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
            ProdutoDal.gravarProdutos(produtos);
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
        for (Produto produto : produtos) {
            if (produto.getIdProduto() == idProduto) {
                if (produto.getEstado() == Constantes.estadosProduto.ATIVO) return true;
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

    public static int determinarEstadoProduto(Produto produto) {
        if (produto.getEstado() != Constantes.estadosProduto.INATIVO) {
            for (Leilao leilao : LeilaoBLL.carregarLeiloes()) {
                if (leilao.getIdProduto() == produto.getIdProduto()) {
                    return Constantes.estadosProduto.RESERVADO;
                }
            }
        }
        return Constantes.estadosProduto.ATIVO;
    }

    public static boolean verificarProdutoEmLeilao(int idProduto) {
        for (Leilao leilao : LeilaoBLL.carregarLeiloes()) {
            if (leilao.getIdProduto() == idProduto) {
                return false;
            }
        }
        return true;
    }
}
