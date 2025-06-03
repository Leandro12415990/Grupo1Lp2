package DAL;

import Model.Categoria;
import Model.Produto;
import Utils.Constantes;
import Utils.DataBaseConnection;
import Utils.Tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAL {
        public List<Categoria> carregarCategoriaCSV() {
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

    public List<Categoria> carregarCategoria() {

        List<Categoria> listaCategoria = new ArrayList<>();
        String sql = "select * from Categoria";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                int id_Categoria = rs.getInt("id_Categoria");
                String descricao = rs.getString("Descricao");
                int estado = rs.getInt("Estado");

                Categoria categoria = new Categoria(id_Categoria, descricao, estado);
                listaCategoria.add(categoria);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaCategoria;
    }
}
