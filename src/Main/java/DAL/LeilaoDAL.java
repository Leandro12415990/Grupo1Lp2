package DAL;

import Model.Leilao;
import Model.Produto;
import Model.Transacao;
import Utils.Constantes.caminhosFicheiros;
import Utils.DataBaseConnection;
import Utils.Tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class LeilaoDAL {
    public List<Leilao> carregaLeiloesCSV() {
        ImportDAL importDal = new ImportDAL();
        return importDal.carregarRegistos(caminhosFicheiros.CSV_FILE_LEILAO, 10, dados -> {
            int id = Integer.parseInt(dados[0]);
            int idProduto = Integer.parseInt(dados[1]);
            String descricao = dados[2];
            int idTipoLeilao = Integer.parseInt(dados[3]);
            LocalDateTime dataInicio = Tools.parseDateTimeByDate(dados[4]);
            LocalDateTime dataFim = dados[5].isEmpty() ? null : Tools.parseDateTimeByDate(dados[5]);
            Double valorMinimo = Double.parseDouble(dados[6]);
            Double valorMaximo = dados[7].isEmpty() ? null : Double.parseDouble(dados[7]);
            Double multiploLance = (dados.length > 8 && !dados[8].isEmpty()) ? Double.parseDouble(dados[8]) : null;
            int idEstado = Integer.parseInt(dados[9]);

            return new Leilao(id, idProduto, descricao, idTipoLeilao, dataInicio, dataFim,
                    valorMinimo, valorMaximo, multiploLance, idEstado);
        });
    }

    public void gravarLeiloesCSV(List<Leilao> leiloes) {
        ImportDAL importDal = new ImportDAL();
        String cabecalho = "ID;ID_PRODUTO;DESCRICAO;ID_TIPO_LEILAO;DATA_INICIO;DATA_FIM;VALOR_MINIMO;VALOR_MAXIMO;MULTIPLO_LANCE;ID_ESTADO";
        importDal.gravarRegistos(caminhosFicheiros.CSV_FILE_LEILAO, cabecalho, leiloes, leilao ->
                leilao.getId() + Tools.separador() +
                        leilao.getIdProduto() + Tools.separador() +
                        leilao.getDescricao() + Tools.separador() +
                        leilao.getTipoLeilao() + Tools.separador() +
                        Tools.formatDateTime(leilao.getDataInicio()) + Tools.separador() +
                        (leilao.getDataFim() != null ? Tools.formatDateTime(leilao.getDataFim()) : "") + Tools.separador() +
                        leilao.getValorMinimo() + Tools.separador() +
                        (leilao.getValorMaximo() != null ? leilao.getValorMaximo() : "") + Tools.separador() +
                        (leilao.getMultiploLance() != null ? leilao.getMultiploLance() : "") + Tools.separador() +
                        leilao.getEstado()
        );
    }

    public List<Leilao> carregaLeiloes() {

        List<Leilao> listaLeilao = new ArrayList<>();
        String sql = "select * from Leilao";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                int id_Leilao = rs.getInt("id_Leilao");
                int id_Produto = rs.getInt("id_Produto");
                String descricao = rs.getString("Descricao");
                int tipoLeilao = rs.getInt("Tipo_Leilao");

                // Converte java.sql.Date para java.time.LocalDate
                LocalDateTime Data_Inicio = rs.getTimestamp("DATA_INICIO") != null ? rs.getTimestamp("DATA_INICIO").toLocalDateTime() : null;

                // Converte java.sql.Date para java.time.LocalDate
                LocalDateTime Data_Fim = rs.getTimestamp("DATA_FIM") != null ? rs.getTimestamp("DATA_FIM").toLocalDateTime() : null;

                double valor_Minimo = rs.getDouble("Valor_Minimo");
                double valor_Maximo = rs.getDouble("Valor_Maximo");
                double Multiplo_Lance = rs.getInt("Multiplo_Lance");
                int Estado_Leilao = rs.getInt("Estado_Leilao");

                Leilao leilao = new Leilao(id_Leilao, id_Produto, descricao, tipoLeilao, Data_Inicio, Data_Fim, valor_Minimo,
                        valor_Maximo, Multiplo_Lance, Estado_Leilao);
                listaLeilao.add(leilao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaLeilao;
    }

    public void gravarLeiloes(List<Leilao> leiloes) {

        String sqlInsert = "INSERT INTO Transacao (id_Produto, Descricao, Tipo_Leilao, Data_Inicio, Data_Fim, Valor_Minimo, Valor_Maximo, Multiplo_Lance, Estado_Leilao) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlUpdate = "UPDATE Transacao SET id_Produto = ?, Descricao = ?, Tipo_Leilao = ?, Data_Inicio = ?, Data_Fim = ?, Valor_Minimo = ?, Valor_Maximo = ?, Multiplo_Lance = ?, Estado_Leilao = ? " +
                "WHERE Id_Leilao = ?";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
                PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
        ) {
            for (Leilao u : leiloes) {
                if (u.getId() == 0) {
                    // INSERT
                    stmtInsert.setInt(1, u.getIdProduto());
                    stmtInsert.setString(2, u.getDescricao());
                    stmtInsert.setDouble(4, u.getTipoLeilao());
                    stmtInsert.setDate(6, u.getDataInicio() != null
                            ? java.sql.Date.valueOf(u.getDataInicio().toLocalDate())
                            : null);
                    stmtInsert.setDate(6, u.getDataFim() != null
                            ? java.sql.Date.valueOf(u.getDataFim().toLocalDate())
                            : null);
                    stmtInsert.setDouble(8, u.getValorMinimo());
                    stmtInsert.setDouble(8, u.getValorMaximo());
                    stmtInsert.setDouble(8, u.getMultiploLance());
                    stmtInsert.setInt(9, u.getEstado());

                    stmtInsert.addBatch();
                } else {
                    // UPDATE
                    stmtUpdate.setInt(1, u.getIdProduto());
                    stmtUpdate.setString(2, u.getDescricao());
                    stmtUpdate.setDouble(4, u.getTipoLeilao());
                    stmtUpdate.setDate(6, u.getDataInicio() != null
                            ? java.sql.Date.valueOf(u.getDataInicio().toLocalDate())
                            : null);
                    stmtUpdate.setDate(6, u.getDataFim() != null
                            ? java.sql.Date.valueOf(u.getDataFim().toLocalDate())
                            : null);
                    stmtUpdate.setDouble(8, u.getValorMinimo());
                    stmtUpdate.setDouble(8, u.getValorMaximo());
                    stmtUpdate.setDouble(8, u.getMultiploLance());
                    stmtUpdate.setInt(9, u.getEstado());

                    stmtUpdate.addBatch();
                }
            }

            stmtInsert.executeBatch();
            stmtUpdate.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
