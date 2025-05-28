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
    public List<Utilizador> carregarUtilizadoresTemp() {
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
                Connection conn = DataBaseConnection.getConnection(); // Usa tua classe aqui
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nomeUtilizador = rs.getString("nomeUtilizador");
                String email = rs.getString("email");

                // Converte java.sql.Date para java.time.LocalDate
                LocalDate dataNascimento = rs.getDate("dataNascimento") != null ? rs.getDate("dataNascimento").toLocalDate() : null;

                String morada = rs.getString("morada");
                String password = rs.getString("password");

                LocalDate dataRegisto = rs.getDate("dataRegisto") != null ? rs.getDate("dataRegisto").toLocalDate() : null;

                LocalDate ultimoLogin = rs.getDate("ultimoLogin") != null ? rs.getDate("ultimoLogin").toLocalDate() : null;

                int tipoUtilizador = rs.getInt("tipoUtilizador");
                int estado = rs.getInt("estado");
                double saldo = rs.getDouble("saldo");

                Utilizador utilizador = new Utilizador(id, nomeUtilizador, email, dataNascimento, morada, password, dataRegisto, ultimoLogin, tipoUtilizador, estado, saldo);
                listaUtilizadores.add(utilizador);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaUtilizadores;
    }

}