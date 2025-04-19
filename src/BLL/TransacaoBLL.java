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
    private static ImportDal importDal = new ImportDal();
    private static List<Transacao> transacaoList = new ArrayList<>();
    private static List<Utilizador> utilizadores = importDal.carregarUtilizador();

    public static List<Transacao> carregarTransacao() {
        ImportDal importDal = new ImportDal();
        transacaoList = importDal.carregarTransacao();
        return transacaoList;
    }

    public static void criarTransacao(Transacao transacao) {
        ImportDal importDal = new ImportDal();
        carregarTransacao();
        transacao.setIdTransacao(verificarUltimoIdCarteira(transacaoList) + 1);
        transacaoList.add(transacao);
        importDal.gravarTransacao(transacaoList);
    }

    private static int verificarUltimoIdCarteira(List<Transacao> transacaoList) {
        int ultimoId = 0;
        for (Transacao transacao : transacaoList) {
            if (transacao.getIdTransacao() > ultimoId) ultimoId = transacao.getIdTransacao();
        }
        return ultimoId;
    }

    public static Double buscarValorTotalAtual(int IdCliente) {
        ImportDal importDal = new ImportDal();
        List<Utilizador> utilizadores = importDal.carregarUtilizador();
        for (Utilizador utilizador : utilizadores) {
            if (utilizador.getId() == IdCliente) return utilizador.getSaldo();
        }
        return 0.0;
    }

    public static double atualizarSaldo(int idCliente, double creditos) {
        ImportDal importDal = new ImportDal();
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


    public static Double valorPendente(int idCliente) {
        ImportDal importDal = new ImportDal();
        transacaoList = importDal.carregarTransacao();
        double valorTotalPendente = 0;
        for (Transacao transacao : transacaoList) {
            if (transacao.getIdCliente() == idCliente && transacao.getIdTipoTransacao() == Constantes.tiposTransacao.DEPOSITO) {
                if (transacao.getIdEstadoTransacao() == Constantes.estadosTransacao.PENDENTE)
                    valorTotalPendente += transacao.getValorTransacao();
            }
        }
        return valorTotalPendente;
    }

    public static List<Transacao> listarTransacoes(boolean apenasPendentes, int idTipoTransacao, int idCliente) {
        carregarTransacao();
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

    public static Utilizador getUtilizador(int idCliente) {
        for (Utilizador utilizador : utilizadores) {
            if (utilizador.getId() == idCliente) return utilizador;
        }
        return null;
    }

    public static Transacao buscarTransacao(int idTransacao) {
        for (Transacao transacao : transacaoList) {
            if (transacao.getIdTransacao() == idTransacao) return transacao;
        }
        return null;
    }

    public static void atualizarEstadosTransacao(int idTransacao, int idEstado) {
        ImportDal importDal = new ImportDal();
        for (Transacao transacao : transacaoList) {
            if (transacao.getIdTransacao() == idTransacao) transacao.setIdEstadoTransacao(idEstado);
        }
        importDal.gravarTransacao(transacaoList);
    }

    public static void devolverSaldo(int idLeilao, int idLanceVencedor) {
        List<Lance> lances = LanceBLL.obterLancesPorLeilao(idLeilao);

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
