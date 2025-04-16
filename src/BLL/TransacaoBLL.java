package BLL;

import DAL.ImportDal;
import Model.Lance;
import Model.Transacao;
import Model.Utilizador;
import Utils.Constantes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransacaoBLL {
    private final ImportDal importDal;
    private final LanceBLL lanceBLL;

    public TransacaoBLL(ImportDal importDal, LanceBLL lanceBLL) {
        this.importDal = importDal;
        this.lanceBLL = lanceBLL;
    }

    public List<Transacao> carregarTransacao() {
        return importDal.carregarTransacao();
    }

    public void criarTransacao(Transacao transacao) {
        List<Transacao> transacaoList = carregarTransacao();
        transacao.setIdTransacao(verificarUltimoIdCarteira(transacaoList) + 1);
        transacaoList.add(transacao);
        importDal.gravarTransacao(transacaoList);
    }

    private int verificarUltimoIdCarteira(List<Transacao> transacaoList) {
        int ultimoId = 0;
        for (Transacao transacao : transacaoList) {
            if (transacao.getIdTransacao() > ultimoId) ultimoId = transacao.getIdTransacao();
        }
        return ultimoId;
    }

    public Double buscarValorTotalAtual(int IdCliente) {
        List<Utilizador> utilizadores = importDal.carregarUtilizador();
        for (Utilizador utilizador : utilizadores) {
            if (utilizador.getId() == IdCliente) return utilizador.getSaldo();
        }
        return 0.0;
    }

    public double atualizarSaldo(int idCliente, double creditos) {
        List<Utilizador> utilizadores = importDal.carregarUtilizador();
        for (Utilizador utilizador : utilizadores) {
            if (utilizador.getId() == idCliente) {
                double saldoAtual = utilizador.getSaldo();
                saldoAtual += creditos;
                utilizador.setSaldo(saldoAtual);
                importDal.gravarUtilizador(utilizadores);
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
        List<Utilizador> utilizadores = importDal.carregarUtilizador();
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
        importDal.gravarTransacao(transacaoList);
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
