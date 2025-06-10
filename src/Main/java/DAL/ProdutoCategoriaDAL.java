package DAL;

import Model.Produto;
import Model.ProdutoCategoria;
import Utils.Constantes;
import Utils.DataBaseConnection;
import Utils.Tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoCategoriaDAL {

    public List<ProdutoCategoria> carregarProdutoCategoriaCSV() {
        ImportDAL importDal = new ImportDAL();
        return importDal.carregarRegistos(Constantes.caminhosFicheiros.CSV_FILE_PRODUTO_CATEGORIA, 2, dados -> {
            int idProduto = Integer.parseInt(dados[0]);
            int idCategoria = Integer.parseInt(dados[1]);
            return new ProdutoCategoria(idProduto,idCategoria);
        });
    }

    public void gravarProdutoCategoriaCSV(List<ProdutoCategoria> categoria) {
        ImportDAL importDal = new ImportDAL();
        String cabecalho = "PRODUTO;CATEGORIA";
        importDal.gravarRegistos(Constantes.caminhosFicheiros.CSV_FILE_PRODUTO_CATEGORIA, cabecalho, categoria, produtoCategoria ->
                produtoCategoria.getIdProduto() + Tools.separador() +
                        produtoCategoria.getIdCategoria()
        );
    }

    public List<ProdutoCategoria> carregarProdutoCategoria() {

        List<ProdutoCategoria> listaProdutoCategoria = new ArrayList<>();
        String sql = "select * from Produto_Categoria";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                int id_produto = rs.getInt("id_Produto");
                int id_categoria = rs.getInt("id_Categoria");

                ProdutoCategoria produtoCategoria = new ProdutoCategoria(id_produto, id_categoria);
                listaProdutoCategoria.add(produtoCategoria);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaProdutoCategoria;
    }

    public void gravarProdutoCategoria(List<ProdutoCategoria> categoria) {
        String sqlInsert = "INSERT INTO Produto_Categoria (id_Produto, id_Categoria) " +
                "VALUES (?, ?)";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
        ) {
            for (ProdutoCategoria u : categoria) {
                // INSERT
                stmtInsert.setInt(1, u.getIdProduto());
                stmtInsert.setInt(2, u.getIdCategoria());

                stmtInsert.addBatch();
            }

            stmtInsert.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
