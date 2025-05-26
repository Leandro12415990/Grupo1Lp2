package DAL;

import Model.Categoria;
import Utils.Constantes;
import Utils.Tools;
import java.util.List;

public class CategoriaDAL {
        public List<Categoria> carregarCategoria() {
            ImportDAL importDal = new ImportDAL();
            return importDal.carregarRegistos(Constantes.caminhosFicheiros.CSV_FILE_CATEGORIA, 3, dados -> {
                int id = Integer.parseInt(dados[0]);
                String descricao = dados[1];
                int estado = Integer.parseInt(dados[2]);
                return new Categoria(id, descricao, estado);
            });
        }

        public void gravarCategoria(List<Categoria> categoria) {
            ImportDAL importDal = new ImportDAL();
            String cabecalho = "ID;DESCRICAO;ESTADO";
            importDal.gravarRegistos(Constantes.caminhosFicheiros.CSV_FILE_CATEGORIA, cabecalho, categoria, categorias ->
                    categorias.getIdCategoria() + Tools.separador() +
                            categorias.getDescricao() + Tools.separador() +
                            categorias.getEstado()
            );
        }
}
