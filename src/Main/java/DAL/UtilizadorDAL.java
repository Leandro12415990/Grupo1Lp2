package DAL;

import Model.Utilizador;
import Utils.Constantes;
import Utils.Tools;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static Utils.DataBaseConnection.getConnection;

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

    public void gravarUtilizadores(List<Utilizador> utilizadores) {
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

    public List<Utilizador> carregarUtilizadores() {
        List<Utilizador> listaUtilizadores = new ArrayList<>();

        String sql = "SELECT * FROM Utilizador";

        try (
                Connection conn = getConnection();
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

    /*public void gravarUtilizadores(List<Utilizador> utilizadores) {
        String sql = "INSERT INTO Utilizador (id, nomeUtilizador, email, dataNascimento, morada, password, dataRegisto," +
                "ultimoLogin, tipoUtilizador, estado, saldo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Utilizador utilizador : utilizadores) {
                stmt.setInt(1, utilizador.getId());
                stmt.setString(2, utilizador.getNomeUtilizador());
                stmt.setString(3, utilizador.getEmail());
                stmt.setDate(4, Date.valueOf(utilizador.getDataNascimento()));
                stmt.setString(5, utilizador.getMorada());
                stmt.setString(6, utilizador.getPassword());
                stmt.setDate(7, Date.valueOf(utilizador.getDataRegisto()));
                stmt.setDate(8, Date.valueOf(utilizador.getUltimoLogin()));
                stmt.setString(9, utilizador.getTipoUtilizador());
                stmt.setString(10, utilizador.getEstado());
                stmt.setDouble(11, utilizador.getSaldo());

                stmt.addBatch(); // Adiciona à batch
            }

            stmt.executeBatch(); // Executa tudo de uma vez
            System.out.println("Utilizadores gravados na base de dados com sucesso.");

        } catch (SQLException e) {
            System.err.println("Erro ao gravar utilizadores na base de dados: " + e.getMessage());
        }
    }*/

    //IMPORTAR UTILIZADORES BY FICHEIRO - PP
    public boolean utilizadorExiste(String email) throws SQLException {
        String sql = "SELECT 1 FROM Utilizador WHERE EMAIL = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int inserirUtilizador(String nome, String email, LocalDate dataNascimento, String morada, String password, LocalDateTime dataRegisto) throws SQLException {
        String sql = "INSERT INTO Utilizador (NOME, EMAIL, DATA_NASCIMENTO, MORADA, PASSWORD, DATA_REGISTO, TIPO_UTILIZADOR, ESTADO, SALDO) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0.0)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setDate(3, java.sql.Date.valueOf(dataNascimento));
            stmt.setString(4, morada);
            stmt.setString(5, password);
            stmt.setTimestamp(6, java.sql.Timestamp.valueOf(dataRegisto));

            stmt.setInt(7, Tools.tipoUtilizador.CLIENTE.getCodigo()); // TIPO_UTILIZADOR
            stmt.setInt(8, Tools.estadoUtilizador.ATIVO.getCodigo()); // ESTADO

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new SQLException("A inserção do utilizador falhou, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("A inserção do utilizador falhou, nenhum ID gerado.");
                }
            }

        }
    }
}