package BLL;

import DAL.CategoriaDAL;
import Model.Categoria;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public class CategoriaBLL {

    public List<Categoria> carregarCategoria() throws MessagingException, IOException {
        CategoriaDAL categoriaDAL = new CategoriaDAL();
        return categoriaDAL.carregarCategoria();
    }

    public void adicionarCategoria(Categoria categoria) throws MessagingException, IOException {
        CategoriaDAL categoriaDAL = new CategoriaDAL();
        List<Categoria> categorias = carregarCategoria();
        categoria.setIdCategoria(verificarUltimoId(categorias) + 1);
        categorias.add(categoria);
        categoriaDAL.gravarCategoria(categorias);
    }

    public boolean editarCategoria(int idCategoria, String novaDescricao) {
        CategoriaDAL categoriaDAL = new CategoriaDAL();
        List<Categoria> categorias = categoriaDAL.carregarCategoria();

        for (Categoria categoria : categorias) {
            if (categoria.getIdCategoria() == idCategoria) {
                categoria.setDescricao(novaDescricao);
                categoriaDAL.gravarCategoria(categorias);
                return true;
            }
        }

        return false;
    }

    public boolean eliminarCategoria(Categoria categoria) {
        CategoriaDAL categoriaDAL = new CategoriaDAL();
        List<Categoria> categorias = obterTodasCategorias();
        boolean categoriaRemovida = false;

        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).getIdCategoria() == categoria.getIdCategoria()) {
                categorias.remove(i);
                categoriaRemovida = true;
                break;
            }
        }
        if (categoriaRemovida) categoriaDAL.gravarCategoria(categorias);
        return categoriaRemovida;
    }

    public Categoria procurarCategoria(int id) {
        List<Categoria> categorias = obterTodasCategorias();
        for (Categoria categoria : categorias) {
            if (categoria.getIdCategoria() == id) return categoria;
        }
        return null;
    }

    public List<Categoria> obterTodasCategorias() {
        CategoriaDAL categoriaDAL = new CategoriaDAL();
        return categoriaDAL.carregarCategoria();
    }

    public List<Categoria> listarCategoria() throws MessagingException, IOException {
        List<Categoria> categorias = carregarCategoria();
        return categorias;
    }

    private int verificarUltimoId(List<Categoria> categorias) {
        int ultimoId = 0;
        for (Categoria categoria : categorias) {
            if (categoria.getIdCategoria() > ultimoId) ultimoId = categoria.getIdCategoria();
        }
        return ultimoId;
    }

    public boolean atualizarCategoriaEstado(int idCategoria, int novoEstado) {
        CategoriaDAL categoriaDAL = new CategoriaDAL();
        List<Categoria> categorias = categoriaDAL.carregarCategoria();

        Categoria categoriaParaAtualizar = null;
        for (Categoria categoria : categorias) {
            if (categoria.getIdCategoria() == idCategoria) {
                categoriaParaAtualizar = categoria;
                break;
            }
        }

        if (categoriaParaAtualizar == null) {
            return false;
        }

        categoriaParaAtualizar.setEstado(novoEstado);
        categoriaDAL.gravarCategoria(categorias);

        return true;
    }

}
