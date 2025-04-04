package BLL;

import DAL.ImportDal;
import Model.Transacao;
import Model.Utilizador;
import Utils.Constantes;

import java.util.ArrayList;
import java.util.List;

public class TransacaoBLL {
    private static List<Transacao> transacaoList = new ArrayList<>();
    private static List<Utilizador> utilizadores = ImportDal.carregarUtilizador();

    public static List<Transacao> carregarTransacao() {
        transacaoList = ImportDal.carregarTransacao();
        return transacaoList;
    }

    public static void criarTransacao(Transacao transacao) {
        carregarTransacao();
        transacao.setIdTransacao(verificarUltimoIdCarteira(transacaoList) + 1);
        transacaoList.add(transacao);
        ImportDal.gravarTransacao(transacaoList);
    }

    private static int verificarUltimoIdCarteira(List<Transacao> transacaoList) {
        int ultimoId = 0;
        for (Transacao transacao : transacaoList) {
            if (transacao.getIdTransacao() > ultimoId) {
                ultimoId = transacao.getIdTransacao();
            }
        } return ultimoId;
    }

    public static Double buscarValorTotalAtual(int IdCliente) {
        List<Utilizador> utilizadores = ImportDal.carregarUtilizador();

        for (Utilizador utilizador : utilizadores) {
            if (utilizador.getId() == IdCliente) {
                return utilizador.getSaldo();
            }
        }
        return 0.0;
    }

    public static void atualizarSaldo(int idCliente, double creditos) {
        for (Utilizador utilizador : utilizadores) {
            if (utilizador.getId() == idCliente) {
                double saldoAtual = utilizador.getSaldo();
                saldoAtual += creditos;
                utilizador.setSaldo(saldoAtual);
            }
        }
        ImportDal.gravarUtilizador(utilizadores);
    }

    public static Double valorPendente(int idCliente) {
        transacaoList = ImportDal.carregarTransacao();
        double valorTotalPendente = 0;
        for (Transacao transacao : transacaoList) {
            if (transacao.getIdCliente() == idCliente) {
                if(transacao.getIdEstadoTransacao() == Constantes.estadosTransacao.PENDENTE)
                    valorTotalPendente += transacao.getValorTransacao();
            }
        } return valorTotalPendente;
    }

    public static List<Transacao> listarTransacoes(boolean apenasPendentes) {
        carregarTransacao();
        if (!apenasPendentes) return transacaoList;

        List<Transacao> transacoesPendentes = new ArrayList<>();
        for (Transacao transacao : transacaoList) {
            if (transacao.getIdEstadoTransacao() == Constantes.estadosTransacao.PENDENTE)
                transacoesPendentes.add(transacao);

        } return transacoesPendentes;
    }

    public static Utilizador getUtilizador(int idCliente) {
        for (Utilizador utilizador : utilizadores) {
            if (utilizador.getId() == idCliente) {
                return utilizador;
            }
        }
        return null;
    }

    public static Transacao buscarTransacao(int idTransacao) {
        for (Transacao transacao : transacaoList) {
            if (transacao.getIdTransacao() == idTransacao) {
                return transacao;
            }
        }
        return null;
    }

    public static void atualizarEstadosTransacao(int idTransacao, int idEstado) {
        for (Transacao transacao : transacaoList) {
            if (transacao.getIdTransacao() == idTransacao) {
                transacao.setIdEstadoTransacao(idEstado);
            }
        }
        ImportDal.gravarTransacao(transacaoList);
    }
}
