package DAL;

import Model.Categoria;
import Utils.Constantes;
import Utils.Tools;
import java.util.List;

public class CategoriaDAL {
        public List<Categoria> carregarCategoria() {
            ImportDAL importDal = new ImportDAL();
            return importDal.carregarRegistos(Constantes.caminhosFicheiros.CSV_FILE_CATEGORIA, 2, dados -> {
                int id = Integer.parseInt(dados[0]);
                String descricao = dados[1];
                return new Categoria(id, descricao);
            });
        }

        public void gravarCategoria(List<Categoria> categoria) {
            ImportDAL importDal = new ImportDAL();
            String cabecalho = "ID;DESCRICAO";
            importDal.gravarRegistos(Constantes.caminhosFicheiros.CSV_FILE_CATEGORIA, cabecalho, categoria, produto ->
                    produto.getIdCategoria() + Tools.separador() +
                            produto.getDescricao()
            );
        }
}
