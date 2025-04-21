package DAL;

import Model.Transacao;
import Utils.Constantes;
import Utils.Tools;

import java.time.LocalDateTime;
import java.util.List;

public class TransacaoDAL {
    public List<Transacao> carregarTransacoes() {
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
}
