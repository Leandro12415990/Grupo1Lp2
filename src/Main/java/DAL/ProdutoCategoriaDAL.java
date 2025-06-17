package DAL;

import Model.ProdutoCategoria;
import Utils.Constantes;
import Utils.Tools;
import java.util.List;

public class ProdutoCategoriaDAL {

    public List<ProdutoCategoria> carregarProdutoCategoria() {
        ImportDAL importDal = new ImportDAL();
        return importDal.carregarRegistos(Constantes.caminhosFicheiros.CSV_FILE_PRODUTO_CATEGORIA, 2, dados -> {
            int idProduto = Integer.parseInt(dados[0]);
            int idCategoria = Integer.parseInt(dados[1]);
            return new ProdutoCategoria(idProduto,idCategoria);
        });
    }

    public void gravarProdutoCategoria(List<ProdutoCategoria> categoria) {
        ImportDAL importDal = new ImportDAL();
        String cabecalho = "PRODUTO;CATEGORIA";
        importDal.gravarRegistos(Constantes.caminhosFicheiros.CSV_FILE_PRODUTO_CATEGORIA, cabecalho, categoria, produtoCategoria ->
                produtoCategoria.getIdProduto() + Tools.separador() +
                        produtoCategoria.getIdCategoria()
        );
    }
}
