package Controller;

import BLL.TransacaoBLL;
import Model.ResultadoOperacao;
import Model.Transacao;
import Model.Utilizador;
import Utils.Constantes;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class TransacaoController {

    public ResultadoOperacao criarTransacao(int idCliente, Double saldoAtual, Double creditos) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        TransacaoBLL transacaoBLL = new TransacaoBLL();
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
        TransacaoBLL transacaoBLL = new TransacaoBLL();
        if (idCliente > 0) return transacaoBLL.buscarValorTotalAtual(idCliente);
        return 0.0;
    }

    public Double valorPendente(int idCliente) {
        TransacaoBLL transacaoBLL = new TransacaoBLL();
        if (idCliente > 0) return transacaoBLL.valorPendente(idCliente);
        return 0.0;
    }

    public List<Transacao> listarDepositos(boolean apenasPendentes, int idTipoTransacao, int idCliente) {
        TransacaoBLL transacaoBLL = new TransacaoBLL();
        List<Transacao> transacaoList = transacaoBLL.listarTransacoes(apenasPendentes, idTipoTransacao, idCliente);
        return transacaoList;
    }

    public Utilizador getUtilizador(int idCliente) {
        TransacaoBLL transacaoBLL = new TransacaoBLL();
        return transacaoBLL.getUtilizador(idCliente);
    }

    public ResultadoOperacao verificarTransacao(int idTransacao) {
        TransacaoBLL transacaoBLL = new TransacaoBLL();
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

    public void atualizarSaldo(int idCliente, Double valorTransacao, char operador) throws MessagingException, IOException {
        TransacaoBLL transacaoBLL = new TransacaoBLL();
        transacaoBLL.atualizarSaldo(idCliente, valorTransacao,operador);
    }

    public Transacao buscarTransacao(int idTransacao) {
        TransacaoBLL transacaoBLL = new TransacaoBLL();
        return transacaoBLL.buscarTransacao(idTransacao);
    }

    public void atualizarEstadosTransacao(int idTransacao, int idEstado) {
        TransacaoBLL transacaoBLL = new TransacaoBLL();
        transacaoBLL.atualizarEstadosTransacao(idTransacao, idEstado);
    }
}
