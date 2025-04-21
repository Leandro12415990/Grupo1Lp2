package Controller;

import BLL.TransacaoBLL;
import Model.Transacao;
import Model.ResultadoOperacao;
import Model.Utilizador;
import Utils.Constantes;
import View.TransacaoView;

import java.time.LocalDateTime;
import java.util.List;

public class TransacaoController {
    public ResultadoOperacao criarTransacao(int idCliente, Double saldoAtual, Double creditos) {

    public TransacaoController(TransacaoBLL transacaoBLL) {
        this.transacaoBLL = transacaoBLL;
    }

    public ResultadoOperacao criarTransacao(int idCliente, Double saldoAtual, Double creditos) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        if (creditos <= 0) {
            resultado.msgErro = "O crédito deve ser positivo.";
        } else {
            Transacao transacao = new Transacao(0, idCliente, saldoAtual, creditos, LocalDateTime.now(), Constantes.tiposTransacao.DEPOSITO, Constantes.estadosTransacao.PENDENTE);
            transacaoBLL.criarTransacao(transacao);

            resultado.Objeto = resultado;
            resultado.Sucesso = true;
        }
        return resultado;
    }

    public Double buscarValorTotalAtual(int idCliente) {
        if (idCliente > 0) return transacaoBLL.buscarValorTotalAtual(idCliente);
        return 0.0;
    }

    public Double valorPendente(int idCliente) {
        if (idCliente > 0) return transacaoBLL.valorPendente(idCliente);
        return 0.0;
    }

    public List<Transacao> listarDepositos(boolean apenasPendentes, int idTipoTransacao, int idCliente) {
        TransacaoView transacaoView = new TransacaoView();
        if (idCliente != 0) transacaoView.exibirTransacoes(transacaoList, true);
        else transacaoView.exibirTransacoes(transacaoList, false);
    }

    public Utilizador getUtilizador(int idCliente) {
        return transacaoBLL.getUtilizador(idCliente);
    }

    public ResultadoOperacao verificarTransacao(int idTransacao) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        List<Transacao> transacaoList = transacaoBLL.listarTransacoes(true, 0, 0);
        for (Transacao transacao : transacaoList) {
            if (idTransacao == transacao.getIdTransacao()) {
                resultado.Objeto = transacao;
                resultado.Sucesso = true;
            } else {
                resultado.msgErro = "O deposito selecionado não está pendente.";
            }
        }
        return resultado;
    }

    public void atualizarSaldo(int idCliente, Double valorTransacao) {
        transacaoBLL.atualizarSaldo(idCliente, valorTransacao);
    }

    public Transacao buscarTransacao(int idTransacao) {
        return transacaoBLL.buscarTransacao(idTransacao);
    }

    public void atualizarEstadosTransacao(int idTransacao, int idEstado) {
        transacaoBLL.atualizarEstadosTransacao(idTransacao, idEstado);
    }
}
