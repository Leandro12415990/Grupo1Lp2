package BLL;

import DAL.ImportDAL;
import DAL.TransacaoDAL;
import DAL.UtilizadorDAL;
import Model.Lance;
import Model.Transacao;
import Model.Utilizador;
import Utils.Constantes;
import Utils.Tools;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransacaoBLL {
    private final TransacaoDAL transacaoDAL;
    private final UtilizadorDAL utilizadorDAL;
    private final LanceBLL lanceBLL;

    public TransacaoBLL(TransacaoDAL transacaoDAL, UtilizadorDAL utilizadorDAL, LanceBLL lanceBLL) {
        this.transacaoDAL = transacaoDAL;
        this.utilizadorDAL = utilizadorDAL;
        this.lanceBLL = lanceBLL;
    }

    public List<Transacao> carregarTransacao() {
        return transacaoDAL.carregarTransacoes();
    }

    public void criarTransacao(Transacao transacao) {
        List<Transacao> transacaoList = carregarTransacao();
        transacao.setIdTransacao(verificarUltimoIdCarteira(transacaoList) + 1);
        transacaoList.add(transacao);
        transacaoDAL.gravarTransacoes(transacaoList);
    }

    private int verificarUltimoIdCarteira(List<Transacao> transacaoList) {
        int ultimoId = 0;
        for (Transacao transacao : transacaoList) {
            if (transacao.getIdTransacao() > ultimoId) ultimoId = transacao.getIdTransacao();
        }
        return ultimoId;
    }

    public Double buscarValorTotalAtual(int IdCliente) {
        List<Utilizador> utilizadores = utilizadorDAL.carregarUtilizadores();
        for (Utilizador utilizador : utilizadores) {
            if (utilizador.getId() == IdCliente) return utilizador.getSaldo();
        }
        return 0.0;
    }

    public double atualizarSaldo(int idCliente, double creditos) {
        List<Utilizador> utilizadores = Tools.utilizadores;
        for (Utilizador utilizador : utilizadores) {
            if (utilizador.getId() == idCliente) {
                double saldoAtual = utilizador.getSaldo();
                saldoAtual += creditos;
                utilizador.setSaldo(saldoAtual);
                utilizadorDAL.gravarUtilizadores(utilizadores);
                return saldoAtual;
            }
        }
        return 0.0;
    }

    public Double valorPendente(int idCliente) {
        List<Transacao> transacaoList = carregarTransacao();
        double valorTotalPendente = 0;
        for (Transacao transacao : transacaoList) {
            if (transacao.getIdCliente() == idCliente && transacao.getIdTipoTransacao() == Constantes.tiposTransacao.DEPOSITO) {
                if (transacao.getIdEstadoTransacao() == Constantes.estadosTransacao.PENDENTE)
                    valorTotalPendente += transacao.getValorTransacao();
            }
        }
        return valorTotalPendente;
    }

    public List<Transacao> listarTransacoes(boolean apenasPendentes, int idTipoTransacao, int idCliente) {
        List<Transacao> transacaoList = carregarTransacao();
        if (!apenasPendentes && idTipoTransacao == 0 && idCliente == 0) return transacaoList;
        List<Transacao> transacoesFiltradas = new ArrayList<>();

        for (Transacao transacao : transacaoList) {
            boolean adicionar = true;

            if (apenasPendentes && transacao.getIdEstadoTransacao() != Constantes.estadosTransacao.PENDENTE)
                adicionar = false;

            if (idTipoTransacao != 0 && transacao.getIdTipoTransacao() != idTipoTransacao)
                adicionar = false;

            if (idCliente != 0 && transacao.getIdCliente() != idCliente)
                adicionar = false;

            if (adicionar)
                transacoesFiltradas.add(transacao);
        }
        return transacoesFiltradas;
    }

    public Utilizador getUtilizador(int idCliente) {
        List<Utilizador> utilizadores = utilizadorDAL.carregarUtilizadores();
        for (Utilizador utilizador : utilizadores) {
            if (utilizador.getId() == idCliente) return utilizador;
        }
        return null;
    }

    public Transacao buscarTransacao(int idTransacao) {
        List<Transacao> transacaoList = carregarTransacao();
        for (Transacao transacao : transacaoList) {
            if (transacao.getIdTransacao() == idTransacao) return transacao;
        }
        return null;
    }

    public void atualizarEstadosTransacao(int idTransacao, int idEstado) {
        List<Transacao> transacaoList = carregarTransacao();
        for (Transacao transacao : transacaoList) {
            if (transacao.getIdTransacao() == idTransacao) transacao.setIdEstadoTransacao(idEstado);
        }
        transacaoDAL.gravarTransacoes(transacaoList);
    }

    public void devolverSaldo(int idLeilao, int idLanceVencedor) {
        List<Lance> lances = lanceBLL.obterLancesPorLeilao(idLeilao);

        for (Lance lance : lances) {
            if (lance.getIdLance() != idLanceVencedor) {
                double saldoAtual = atualizarSaldo(lance.getIdCliente(), lance.getValorLance());
                if (saldoAtual != 0.0) {
                    Transacao transacao = new Transacao(0, lance.getIdCliente(), saldoAtual, lance.getValorLance(), LocalDateTime.now(), Constantes.tiposTransacao.LANCE_REEMBOLSO, Constantes.estadosTransacao.ACEITE);
                    criarTransacao(transacao);
                }
            }
        }
    }
}
