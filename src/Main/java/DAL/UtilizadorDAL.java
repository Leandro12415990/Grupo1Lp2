package DAL;

import Model.Utilizador;
import Utils.Constantes;
import Utils.DataBaseConnection;
import Utils.Tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UtilizadorDAL {
    public List<Utilizador> carregarUtilizadoresCSV() {
        ImportDAL importDal = new ImportDAL();
        return importDal.carregarRegistos(Constantes.caminhosFicheiros.CSV_FILE_UTILIZADOR, 11, dados -> {
            int id = Integer.parseInt(dados[0]);
            String nomeUtilizador = dados[1];
            String email = dados[2];
            LocalDate dataNascimento = Tools.parseDate(dados[3]);
            String morada = dados[4];
            String password = dados[5];
            LocalDate dataRegisto = Tools.parseDate(dados[6]);
            LocalDate ultimoLogin = dados[7].isEmpty() ? null : Tools.parseDate(dados[7]);
            int tipoUtilizador = Integer.parseInt(dados[8]);
            int estado = Integer.parseInt(dados[9]);
            Double saldo = Double.parseDouble(dados[10]);
            return new Utilizador(id, nomeUtilizador, email, dataNascimento, morada, password, dataRegisto, ultimoLogin, tipoUtilizador, estado, saldo);
        });
    }

    public void gravarUtilizadoresCSV(List<Utilizador> utilizadores) {
        ImportDAL importDal = new ImportDAL();
        String cabecalho = "ID;NOME;EMAIL;DATA NASCIMENTO;MORADA;PASSWORD;DATA REGISTO;ULTIMO LOGIN;TIPO UTILIZADOR;ESTADO;SALDO";
        importDal.gravarRegistos(Constantes.caminhosFicheiros.CSV_FILE_UTILIZADOR, cabecalho, utilizadores, utilizador ->
                utilizador.getId() + Tools.separador() +
                        utilizador.getNomeUtilizador() + Tools.separador() +
                        utilizador.getEmail() + Tools.separador() +
                        Tools.formatDate(utilizador.getDataNascimento()) + Tools.separador() +
                        utilizador.getMorada() + Tools.separador() +
                        utilizador.getPassword() + Tools.separador() +
                        Tools.formatDate(utilizador.getDataRegisto()) + Tools.separador() +
                        Tools.formatDate(utilizador.getUltimoLogin()) + Tools.separador() +
                        utilizador.getTipoUtilizador() + Tools.separador() +
                        utilizador.getEstado() + Tools.separador() +
                        utilizador.getSaldo()
        );
    }

    public void gravarUtilizadores(List<Utilizador> utilizadores) {
        String sqlInsert = "INSERT INTO Utilizador (Nome, Email, Data_Nascimento, Morada, Password, Data_Registo, Ultimo_Login, Tipo_Utilizador, Estado, Saldo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlUpdate = "UPDATE Utilizador SET Nome = ?, Email = ?, Data_Nascimento = ?, Morada = ?, Password = ?, Data_Registo = ?, Ultimo_Login = ?, Tipo_Utilizador = ?, Estado = ?, Saldo = ? " +
                "WHERE id_Utilizador = ?";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
                PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
        ) {
            for (Utilizador u : utilizadores) {
                if (u.getId() == 0) {
                    // INSERT
                    stmtInsert.setString(1, u.getNomeUtilizador());
                    stmtInsert.setString(2, u.getEmail());
                    stmtInsert.setDate(3, u.getDataNascimento() != null ? java.sql.Date.valueOf(u.getDataNascimento()) : null);
                    stmtInsert.setString(4, u.getMorada());
                    stmtInsert.setString(5, u.getPassword());
                    stmtInsert.setDate(6, u.getDataRegisto() != null ? java.sql.Date.valueOf(u.getDataRegisto()) : null);
                    stmtInsert.setDate(7, u.getUltimoLogin() != null ? java.sql.Date.valueOf(u.getUltimoLogin()) : null);
                    stmtInsert.setInt(8, u.getTipoUtilizador());
                    stmtInsert.setInt(9, u.getEstado());
                    stmtInsert.setDouble(10, u.getSaldo());

                    stmtInsert.addBatch();
                } else {
                    // UPDATE
                    stmtUpdate.setString(1, u.getNomeUtilizador());
                    stmtUpdate.setString(2, u.getEmail());
                    stmtUpdate.setDate(3, u.getDataNascimento() != null ? java.sql.Date.valueOf(u.getDataNascimento()) : null);
                    stmtUpdate.setString(4, u.getMorada());
                    stmtUpdate.setString(5, u.getPassword());
                    stmtUpdate.setDate(6, u.getDataRegisto() != null ? java.sql.Date.valueOf(u.getDataRegisto()) : null);
                    stmtUpdate.setDate(7, u.getUltimoLogin() != null ? java.sql.Date.valueOf(u.getUltimoLogin()) : null);
                    stmtUpdate.setInt(8, u.getTipoUtilizador());
                    stmtUpdate.setInt(9, u.getEstado());
                    stmtUpdate.setDouble(10, u.getSaldo());
                    stmtUpdate.setInt(11, u.getId());

                    stmtUpdate.addBatch();
                }
            }

            stmtInsert.executeBatch();
            stmtUpdate.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Utilizador> carregarUtilizadores() {
        List<Utilizador> listaUtilizadores = new ArrayList<>();

        String sql = "SELECT * FROM Utilizador";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                int id = rs.getInt("id_Utilizador");
                String nomeUtilizador = rs.getString("Nome");
                String email = rs.getString("EMAIl");

                // Converte java.sql.Date para java.time.LocalDate
                LocalDate dataNascimento = rs.getDate("DATA_NASCIMENTO") != null ? rs.getDate("DATA_NASCIMENTO").toLocalDate() : null;

                String morada = rs.getString("MORADA");
                String password = rs.getString("PASSWORD");

                LocalDate dataRegisto = rs.getDate("DATA_REGISTO") != null ? rs.getDate("DATA_REGISTO").toLocalDate() : null;

                LocalDate ultimoLogin = rs.getDate("ULTIMO_LOGIN") != null ? rs.getDate("ULTIMO_LOGIN").toLocalDate() : null;

                int tipoUtilizador = rs.getInt("TIPO_UTILIZADOR");
                int estado = rs.getInt("ESTADO");
                double saldo = rs.getDouble("SALDO");

                Utilizador utilizador = new Utilizador(id, nomeUtilizador, email, dataNascimento, morada, password, dataRegisto, ultimoLogin, tipoUtilizador, estado, saldo);
                listaUtilizadores.add(utilizador);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaUtilizadores;
    }
}