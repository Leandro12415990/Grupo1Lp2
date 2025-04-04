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
    public static ResultadoOperacao criarTransacao(int idCliente, Double saldoAtual, Double creditos) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        if (creditos <= 0) {
            resultado.msgErro = "O crédito deve ser positivo.";
        } else {
            Transacao transacao = new Transacao(0, idCliente, saldoAtual, creditos, LocalDateTime.now(), Constantes.tiposTransacao.DEPOSITO, Constantes.estadosTransacao.PENDENTE);
            TransacaoBLL.criarTransacao(transacao);

            resultado.Objeto = resultado;
            resultado.Sucesso = true;
        }
        return resultado;
    }

    public static Double buscarValorTotalAtual(int idCliente) {
        if (idCliente > 0) {
            return TransacaoBLL.buscarValorTotalAtual(idCliente);
        }
        return 0.0;
    }

    public static Double valorPendente(int idCliente) {
        if (idCliente > 0) {
            return TransacaoBLL.valorPendente(idCliente);
        }
        return 0.0;
    }

    public static void listarDepositos(boolean apenasPendentes, int idTipoTransacao, int idCliente) {
        List<Transacao> transacaoList = TransacaoBLL.listarTransacoes(apenasPendentes, idTipoTransacao, idCliente);
        TransacaoView.exibirTransacoes(transacaoList);
    }

    public static Utilizador getUtilizador(int idCliente) {
        return TransacaoBLL.getUtilizador(idCliente);
    }

    public static ResultadoOperacao verificarTransacao(int idTransacao) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        List<Transacao> transacaoList = TransacaoBLL.listarTransacoes(true, 0, 0);
        for (Transacao transacao : transacaoList) {
            if (idTransacao == transacao.getIdTransacao()) {
                resultado.Objeto = transacao;
                resultado.Sucesso = true;
            } else
                resultado.msgErro = "O deposito selecionado não está pendente.";
        }

        return resultado;
    }

    public static void atualizarSaldo(int idCliente, Double valorTransacao) {
        TransacaoBLL.atualizarSaldo(idCliente, valorTransacao);
    }

    public static Transacao buscarTransacao(int idTransacao) {
        return TransacaoBLL.buscarTransacao(idTransacao);
    }

    public static void atualizarEstadosTransacao(int idTransacao, int idEstado) {
        TransacaoBLL.atualizarEstadosTransacao(idTransacao, idEstado);
    }

}
