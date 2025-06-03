package DAL;

import Model.Transacao;
import Model.Utilizador;
import Utils.Constantes;
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

public class TransacaoDAL {
    public List<Transacao> carregarTransacoesCSV() {
        ImportDAL importDal = new ImportDAL();
        return importDal.carregarRegistos(Constantes.caminhosFicheiros.CSV_FILE_TRANSACAO, 7, dados -> {
            int idTransacao = Integer.parseInt(dados[0]);
            int idCliente = Integer.parseInt(dados[1]);
            Double valorTotal = Double.parseDouble(dados[2]);
            Double valorTransacao = Double.parseDouble(dados[3]);
            LocalDateTime dataTransacao = Tools.parseDateTimeByDate(dados[4]);
            int idTipoTransacao = Integer.parseInt(dados[5]);
            int idEstadoTransacao = Integer.parseInt(dados[6]);
            return new Transacao(idTransacao, idCliente, valorTotal, valorTransacao, dataTransacao, idTipoTransacao, idEstadoTransacao);
        });
    }

    public void gravarTransacoes(List<Transacao> transacoes) {
        ImportDAL importDal = new ImportDAL();
        String cabecalho = "ID_TRANSACAO;ID_CLIENTE;VALOR_TOTAL;VALOR_TRANSACAO;DATA_TRANSACAO;ID_TIPO;ID_ESTADO";
        importDal.gravarRegistos(Constantes.caminhosFicheiros.CSV_FILE_TRANSACAO, cabecalho, transacoes, transacao ->
                transacao.getIdTransacao() + Tools.separador() +
                        transacao.getIdCliente() + Tools.separador() +
                        transacao.getValorTotal() + Tools.separador() +
                        transacao.getValorTransacao() + Tools.separador() +
                        Tools.formatDateTime(transacao.getDataTransacao()) + Tools.separador() +
                        transacao.getIdTipoTransacao() + Tools.separador() +
                        transacao.getIdEstadoTransacao()
        );
    }

    public List<Transacao> carregarTransacoes() {
        List<Transacao> listaTransacoes = new ArrayList<>();

        String sql = "SELECT * FROM Transacao";

        try (
                Connection conn = DataBaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                int id_transacao = rs.getInt("ID_TRANSACAO");
                int id_cliente = rs.getInt("ID_CLIENTE");
                double valor_total = rs.getDouble("VALOR_TOTAL");
                double valor_transacao = rs.getDouble("VALOR_TRANSACAO");

                // Converte java.sql.Date para java.time.LocalDateTime
                LocalDateTime data_transacao = rs.getTimestamp("DATA_TRANSACAO") != null
                        ? rs.getTimestamp("DATA_TRANSACAO").toLocalDateTime()
                        : null;

                int tipo_transacao = rs.getInt("TIPO_TRANSACAO");
                int estado = rs.getInt("ESTADO");

                Transacao transacao = new Transacao(id_transacao, id_cliente, valor_total, valor_transacao, data_transacao,
                        tipo_transacao, estado);
                listaTransacoes.add(transacao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaTransacoes;
    }
}

