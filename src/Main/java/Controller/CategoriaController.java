package Controller;

import BLL.CategoriaBLL;
import BLL.ProdutoCategoriaBLL;
import Model.Categoria;
import Model.ResultadoOperacao;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public class CategoriaController {
    public ResultadoOperacao criarCategoria(int idCategoria, String descricao, int estado) throws MessagingException, IOException {
        CategoriaBLL categoriaBLL = new CategoriaBLL();
        ResultadoOperacao resultado = new ResultadoOperacao();
        if (descricao == null || descricao.isEmpty()) {
            resultado.msgErro = "A descrição da categoria é de preenchimento obrigatório!";
        } else {
            Categoria novaCategoria = new Categoria(idCategoria, descricao, estado);
            categoriaBLL.adicionarCategoria(novaCategoria);

            resultado.Objeto = resultado;
            resultado.Sucesso = true;
        }
        return resultado;
    }

    public List<Categoria> listarCategoria() throws MessagingException, IOException {
        CategoriaBLL categoriaBLL = new CategoriaBLL();
        return categoriaBLL.listarCategoria();
    }

    public boolean editarCategoria(int idCategoria, String descricao) {
        CategoriaBLL categoriaBLL = new CategoriaBLL();
        return categoriaBLL.editarCategoria(idCategoria,descricao);
    }

    public boolean eliminarCategoria(Categoria categoria) {
        CategoriaBLL categoriaBLL = new CategoriaBLL();
        boolean sucesso = categoriaBLL.eliminarCategoria(categoria);

        if (sucesso) categoriaBLL.obterTodasCategorias();
        return sucesso;
    }

    public Categoria procurarCategoria(int id) {
        CategoriaBLL categoriaBLL = new CategoriaBLL();
        if (id > 0) return categoriaBLL.procurarCategoria(id);
        return null;
    }

    public boolean atualizarCategoriaEstado(int idCategoria, int novoEstado) {
        CategoriaBLL bll = new CategoriaBLL();
        return bll.atualizarCategoriaEstado(idCategoria, novoEstado);
    }


}
