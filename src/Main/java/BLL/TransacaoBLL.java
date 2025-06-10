package BLL;

import DAL.TemplateDAL;
import DAL.TransacaoDAL;
import DAL.UtilizadorDAL;
import Model.*;
import Utils.Constantes;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransacaoBLL {
    private static List<Transacao> transacaoList = new ArrayList<>();

    public static List<Transacao> carregarTransacao() {
        TransacaoDAL transacaoDAL = new TransacaoDAL();
        transacaoList = transacaoDAL.carregarTransacoes();
        return transacaoList;
    }

    public void criarTransacao(Transacao transacao) {
        TransacaoDAL transacaoDAL = new TransacaoDAL();
        List<Transacao> lista = carregarTransacao();
        transacao.setIdTransacao(verificarUltimoIdCarteira(lista) + 1);
        lista.add(transacao);
        transacaoDAL.gravarTransacoes(lista);
        transacaoList = lista;
    }

    private int verificarUltimoIdCarteira(List<Transacao> lista) {
        int ultimoId = 0;
        for (Transacao transacao : lista) {
            if (transacao.getIdTransacao() > ultimoId) ultimoId = transacao.getIdTransacao();
        }
        return ultimoId;
    }

    public Double buscarValorTotalAtual(int IdCliente) {
        UtilizadorDAL utilizadorDAL = new UtilizadorDAL();
        List<Utilizador> utilizadores = utilizadorDAL.carregarUtilizadores();
        for (Utilizador utilizador : utilizadores) {
            if (utilizador.getId() == IdCliente) return utilizador.getSaldo();
        }
        return 0.0;
    }

    public double atualizarSaldo(int idCliente, double creditos, char operador, boolean isDevolucao, boolean criarTransacao) throws IOException, MessagingException {
        UtilizadorDAL utilizadorDAL = new UtilizadorDAL();
        TemplateDAL templateDAL = new TemplateDAL();
        EmailBLL emailBLL = new EmailBLL();
        Transacao transacao = null;
        List<Utilizador> utilizadorList = utilizadorDAL.carregarUtilizadores();

        for (Utilizador utilizador : utilizadorList) {
            if (utilizador.getId() == idCliente) {
                double saldoAtual = utilizador.getSaldo();
                switch (operador) {
                    case '+':
                        saldoAtual += creditos;
                        if (criarTransacao) {
                            if (isDevolucao) {
                                transacao = new Transacao(0, idCliente, saldoAtual, creditos, LocalDateTime.now(), Constantes.tiposTransacao.LANCE_REEMBOLSO, Constantes.estadosTransacao.ACEITE);
                            } else {
                                transacao = new Transacao(0, idCliente, saldoAtual, creditos, LocalDateTime.now(), Constantes.tiposTransacao.DEPOSITO, Constantes.estadosTransacao.ACEITE);
                            }
                        }
                        break;
                    case '-':
                        saldoAtual -= creditos;
                        if (criarTransacao) {
                            transacao = new Transacao(0, idCliente, saldoAtual, creditos, LocalDateTime.now(), Constantes.tiposTransacao.LANCE_DEBITO, Constantes.estadosTransacao.ACEITE);
                        }
                        break;
                }
                utilizador.setSaldo(saldoAtual);
                if (criarTransacao && transacao != null) criarTransacao(transacao);
                if (saldoAtual <= 0.0) {
                    Template template = templateDAL.carregarTemplatePorId(Constantes.templateIds.EMAIL_SEM_CREDITOS);
                    if (template != null)
                        emailBLL.enviarEmail(template, utilizador.getEmail(), Tools.substituirTags(utilizador, null, null), utilizador.getId());
                }
                utilizadorDAL.gravarUtilizadores(utilizadorList);
                return saldoAtual;
            }
        }
        return 0.0;
    }

    public Double valorPendente(int idCliente) {
        List<Transacao> lista = carregarTransacao();
        double totalPendente = 0;
        for (Transacao t : lista) {
            if (t.getIdCliente() == idCliente &&
                    t.getIdTipoTransacao() == Constantes.tiposTransacao.DEPOSITO &&
                    t.getIdEstadoTransacao() == Constantes.estadosTransacao.PENDENTE) {
                totalPendente += t.getValorTransacao();
            }
        }
        return totalPendente;
    }

    public List<Transacao> listarTransacoes(boolean apenasPendentes, int idTipoTransacao, int idCliente) {
        List<Transacao> lista = carregarTransacao();
        if (!apenasPendentes && idTipoTransacao == 0 && idCliente == 0) return lista;

        List<Transacao> filtradas = new ArrayList<>();
        for (Transacao t : lista) {
            boolean incluir = true;

            if (apenasPendentes && t.getIdEstadoTransacao() != Constantes.estadosTransacao.PENDENTE)
                incluir = false;
            if (idTipoTransacao != 0 && t.getIdTipoTransacao() != idTipoTransacao)
                incluir = false;
            if (idCliente != 0 && t.getIdCliente() != idCliente)
                incluir = false;

            if (incluir) filtradas.add(t);
        }
        return filtradas;
    }

    public Utilizador getUtilizador(int idCliente) {
        UtilizadorDAL utilizadorDAL = new UtilizadorDAL();
        for (Utilizador u : utilizadorDAL.carregarUtilizadores()) {
            if (u.getId() == idCliente) return u;
        }
        return null;
    }

    public Transacao buscarTransacao(int idTransacao) {
        for (Transacao t : carregarTransacao()) {
            if (t.getIdTransacao() == idTransacao) return t;
        }
        return null;
    }

    public void atualizarEstadosTransacao(int idTransacao, int idEstado) {
        TransacaoDAL transacaoDAL = new TransacaoDAL();
        for (Transacao transacao : transacaoList) {
            if (transacao.getIdTransacao() == idTransacao) transacao.setIdEstadoTransacao(idEstado);
        }
        transacaoDAL.gravarTransacoes(transacaoList);
    }

    public void devolverSaldo(int idLeilao, int idLanceVencedor) throws MessagingException, IOException {
        LanceBLL lanceBLL = new LanceBLL();
        List<Lance> lances = lanceBLL.obterLancesPorLeilao(idLeilao);
        char operador = '+';
        for (Lance lance : lances) {
            if (lance.getIdLance() != idLanceVencedor)
                atualizarSaldo(lance.getIdCliente(), lance.getValorLance(), operador, true, true);
        }
    }

    public void reembolsarUltimoLanceEletronico(int idLeilao) throws IOException, MessagingException {
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);
        if (leilao == null || leilao.getIdTipoLeilao() != Constantes.tiposLeilao.ELETRONICO) return;

        LanceBLL lanceBLL = new LanceBLL();
        List<Lance> lances = lanceBLL.obterLancesPorLeilao(idLeilao);
        Lance ultimoLance = null;
        for (Lance l : lances) {
            if (ultimoLance == null || l.getDataLance().isAfter(ultimoLance.getDataLance())) ultimoLance = l;
        }
        if (ultimoLance != null)
            atualizarSaldo(ultimoLance.getIdCliente(), ultimoLance.getValorLance(), '+', true, true);
    }
}
