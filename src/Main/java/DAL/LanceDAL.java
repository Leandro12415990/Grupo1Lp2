package DAL;

import Model.Lance;
import Model.Produto;
import Model.Utilizador;
import Utils.Constantes.caminhosFicheiros;
import Utils.DataBaseConnection;
import Utils.Tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class LanceDAL {
    public List<Lance> carregarLancesCSV() {
        ImportDAL importDal = new ImportDAL();
        return importDal.carregarRegistos(caminhosFicheiros.CSV_FILE_LANCE, 7, dados -> {
            int idLance = Integer.parseInt(dados[0]);
            int idLeilao = Integer.parseInt(dados[1]);
            int idCliente = Integer.parseInt(dados[2]);
            double valorLance = Double.parseDouble(dados[3]);
            int idNegociacao = dados[4].isEmpty() ? 0 : Integer.parseInt(dados[4]);
            double valorContraProposta = Double.parseDouble(dados[5]);
            LocalDateTime dataLance = Tools.parseDateTimeByDate(dados[6]);
            int estado = Integer.parseInt(dados[7]);
            return new Lance(idLance, idLeilao, idCliente, valorLance, idNegociacao, valorContraProposta, dataLance, estado);
        });
    }

    public void gravarLancesCSV(List<Lance> lances) {
        ImportDAL importDal = new ImportDAL();
        String cabecalho = "ID APOSTA;ID LEILÃO;ID CLIENTE;VALOR APOSTA;MULTIPLOS UTILIZADOS;PONTOS UTILIZADOS;DATA APOSTA";
        importDal.gravarRegistos(caminhosFicheiros.CSV_FILE_LANCE, cabecalho, lances, lance ->
                String.join(Tools.separador(),
                        String.valueOf(lance.getIdLance()),
                        String.valueOf(lance.getIdLeilao()),
                        String.valueOf(lance.getIdCliente()),
                        String.valueOf(lance.getValorLance()),
                        String.valueOf(lance.getNumLance()),
                        String.valueOf(lance.getPontosUtilizados()),
                        Tools.formatDateTime(lance.getDataLance())
                )
        );
    }

    public List<Lance> carregarLances() {

        List<Lance> listaLance = new ArrayList<>();
        String sql = "select * from Lance";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                int idLance = rs.getInt("id_Lance");
                int idLeilao = rs.getInt("id_Leilao");
                int idCliente = rs.getInt("id_Cliente");
                double valorLance = rs.getDouble("Valor_Aposta");
                int Multiplos_Utilzadores = rs.getInt("Multiplos_Utilizados");
                int pontosUtilizados = rs.getInt("Pontos_Utilizados");

                // Converte java.sql.Date para java.time.LocalDate
                LocalDateTime dataAposta = rs.getTimestamp("DATA_Aposta") != null ? rs.getTimestamp("DATA_Aposta").toLocalDateTime() : null;

                Lance lance = new Lance(idLance, idLeilao, idCliente, valorLance, Multiplos_Utilzadores, pontosUtilizados, dataAposta);
                listaLance.add(lance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaLance;
    }

    public void gravarLances(List<Lance> lances) {
        ImportDAL importDal = new ImportDAL();
        String cabecalho = "ID APOSTA;ID LEILÃO;ID CLIENTE;VALOR APOSTA;ID NEGOCIACAO;VALOR CONTRAPROPOSTA;DATA APOSTA;ESTADO";
        importDal.gravarRegistos(caminhosFicheiros.CSV_FILE_LANCE, cabecalho, lances, lance ->
                String.join(Tools.separador(),
                        String.valueOf(lance.getIdLance()),
                        String.valueOf(lance.getIdLeilao()),
                        String.valueOf(lance.getIdCliente()),
                        String.valueOf(lance.getValorLance()),
                        String.valueOf(lance.getIdNegociacao()),
                        String.valueOf(lance.getValorContraProposta()),
                        Tools.formatDateTime(lance.getDataLance()),
                        String.valueOf(lance.getEstado())
                )
        );

        String sqlInsert = "INSERT INTO Lance (id_Leilao, id_Cliente, Valor_Aposta, Multiplos_Utilizados, Pontos_Utilizados, Data_Aposta) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        String sqlUpdate = "UPDATE Lance SET id_Leilao = ?, id_Cliente = ?, Valor_Aposta = ?, Multiplos_Utilizados = ?, Pontos_Utilizados = ?, Data_Aposta = ? " +
                "WHERE id_Lance = ?";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
                PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
        ) {
            for (Lance u : lances) {
                if (u.getIdLance() == 0) {
                    // INSERT
                    stmtInsert.setInt(1, u.getIdLance());
                    stmtInsert.setInt(2, u.getIdLeilao());
                    stmtInsert.setInt(4, u.getIdCliente());
                    stmtInsert.setDouble(5, u.getValorLance());
                    stmtInsert.setDouble(5, u.getNumLance());
                    stmtInsert.setDouble(5, u.getPontosUtilizados());
                    stmtInsert.setDate(6, u.getDataLance() != null
                            ? java.sql.Date.valueOf(u.getDataLance().toLocalDate())
                            : null);

                    stmtInsert.addBatch();
                } else {
                    // UPDATE
                    stmtUpdate.setInt(1, u.getIdLance());
                    stmtUpdate.setInt(2, u.getIdLeilao());
                    stmtUpdate.setInt(4, u.getIdCliente());
                    stmtUpdate.setDouble(5, u.getValorLance());
                    stmtUpdate.setDouble(5, u.getNumLance());
                    stmtUpdate.setDouble(5, u.getPontosUtilizados());
                    stmtUpdate.setDate(6, u.getDataLance() != null
                            ? java.sql.Date.valueOf(u.getDataLance().toLocalDate())
                            : null);

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
