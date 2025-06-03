package DAL;

import Model.Produto;
import Model.Template;
import Utils.Constantes.caminhosFicheiros;
import Utils.DataBaseConnection;
import Utils.Tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ProdutoDAL {
    public List<Produto> carregarProdutosCSV() {
        ImportDAL importDal = new ImportDAL();
        return importDal.carregarRegistos(caminhosFicheiros.CSV_FILE_PRODUTO, 4, dados -> {
            int id = Integer.parseInt(dados[0]);
            int estado = Integer.parseInt(dados[1]);
            String nome = dados[2];
            String descricao = dados[3];
            return new Produto(id, estado, nome, descricao);
        });
    }

    public void gravarProdutos(List<Produto> produtos) {
        ImportDAL importDal = new ImportDAL();
        String cabecalho = "ID;ESTADO;NOME;DESCRICAO";
        importDal.gravarRegistos(caminhosFicheiros.CSV_FILE_PRODUTO, cabecalho, produtos, produto ->
                produto.getIdProduto() + Tools.separador() +
                        produto.getEstado() + Tools.separador() +
                        produto.getNome() + Tools.separador() +
                        produto.getDescricao()
        );
    }

    public List<Produto> carregarProdutos() {

        List<Produto> listaProduto = new ArrayList<>();
        String sql = "select * from Produto";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                int id = rs.getInt("id_Produto");
                int estado = rs.getInt("Estado");
                String nome = rs.getString("Nome");
                String descricao = rs.getString("Descricao");

                Produto produto = new Produto(id, estado, nome, descricao);
                listaProduto.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaProduto;
    }
}
