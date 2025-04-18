package BLL;

import DAL.ImportDAL;
import DAL.LanceDAL;
import DAL.UtilizadorDAL;
import Model.*;
import Utils.Constantes;
import Utils.Tools;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LanceBLL {
    private final LanceDAL lanceDAL;
    private final UtilizadorDAL utilizadorDAL;
    private final List<Lance> lances;
    private final LeilaoBLL leilaoBLL;
    private final UtilizadorBLL utilizadorBLL;
    private TransacaoBLL transacaoBLL;

    public LanceBLL(LanceDAL lanceDAL, UtilizadorDAL utilizadorDAL, LeilaoBLL leilaoBLL, UtilizadorBLL utilizadorBLL) {
        this.lanceDAL = lanceDAL;
        this.utilizadorDAL = utilizadorDAL;
        this.leilaoBLL = leilaoBLL;
        this.utilizadorBLL = utilizadorBLL;
        this.lances = lanceDAL.carregarLances();
    }

    public void setTransacaoBLL(TransacaoBLL transacaoBLL) {
        this.transacaoBLL = transacaoBLL;
    }

    public ResultadoOperacao adicionarLanceDireto(int idLeilao, double valorLance, int idCliente, int idTipoLeilao) {
        ResultadoOperacao resultado = new ResultadoOperacao();

        ResultadoOperacao saldoVerificado = verificarSaldoEAtualizar(idCliente, valorLance, idTipoLeilao);
        if (!saldoVerificado.Sucesso) {
            return saldoVerificado;
        }

        int idLance = verUltimoId() + 1;
        Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);

        int numLance = 0;
        int pontosUtilizados = 0;
        LocalDateTime dataLance = LocalDateTime.now();

        Lance lance = new Lance(idLance, idLeilao, idCliente, valorLance, numLance, pontosUtilizados, dataLance);
        lances.add(lance);
        lanceDAL.gravarLances(lances);

        if (valorLance == leilao.getValorMinimo()) {
            fimLeilao(idLeilao, dataLance);
        }

        resultado.Sucesso = true;
        resultado.Objeto = lance;
        return resultado;
    }

    public ResultadoOperacao adicionarLanceCartaFechada(int idLeilao, double valorLance, int idCliente, int idTipoLeilao) {
        ResultadoOperacao resultado = new ResultadoOperacao();

        ResultadoOperacao saldoVerificado = verificarSaldoEAtualizar(idCliente, valorLance, idTipoLeilao);
        if (!saldoVerificado.Sucesso) {
            return saldoVerificado;
        }

        int idLance = verUltimoId() + 1;
        Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);

        int numLance = 0;
        int pontosUtilizados = 0;
        LocalDateTime dataLance = LocalDateTime.now();

        Lance lance = new Lance(idLance, idLeilao, idCliente, valorLance, numLance, pontosUtilizados, dataLance);
        lances.add(lance);
        lanceDAL.gravarLances(lances);

        resultado.Sucesso = true;
        resultado.Objeto = lance;
        return resultado;
    }

    public ResultadoOperacao adicionarLanceEletronico(int idLeilao, double valorLance, int numLance, double multiploLance, int idCliente, int valorLanceAtual, int idTipoLeilao) {
        ResultadoOperacao resultado = new ResultadoOperacao();

        valorLance += (multiploLance * numLance);

        ResultadoOperacao saldoVerificado = verificarSaldoEAtualizar(idCliente, valorLance, idTipoLeilao);
        if (!saldoVerificado.Sucesso) {
            return saldoVerificado;
        }

        int idLance = verUltimoId() + 1;
        Leilao leilao = leilaoBLL.procurarLeilaoPorId(idTipoLeilao);

        int pontosUtilizados = 0;
        LocalDateTime dataLance = LocalDateTime.now();

        Lance lance = new Lance(idLance, idLeilao, idCliente, valorLance, numLance, pontosUtilizados, dataLance);
        lances.add(lance);
        lanceDAL.gravarLances(lances);

        resultado.Sucesso = true;
        resultado.Objeto = lance;
        return resultado;
    }

    public int verUltimoId() {
        int ultimoId = 0;
        for (Lance lance : lances) {
            if (lance.getIdLance() > ultimoId) {
                ultimoId = lance.getIdLance();
            }
        }
        return ultimoId;
    }

    public List<Lance> listarMeuLance(int IdCliente) {
        List<Lance> lancesByCliente = new ArrayList<>();
        for (Lance lance : lances) {
            if (lance.getIdCliente() == IdCliente) {
                lancesByCliente.add(lance);
            }
        }
        return lancesByCliente;
    }

    public List<Lance> obterLancesPorLeilao(int idLeilao) {
        List<Lance> lancesByLeilao = new ArrayList<>();
        for (Lance lance : lances) {
            if (idLeilao == 0) {
                lancesByLeilao.add(lance);
            } else if(lance.getIdLeilao() == idLeilao){
                lancesByLeilao.add(lance);
            }
        }
        return lancesByLeilao;
    }

    public void fimLeilao(int idLeilao, LocalDateTime dataFim) {
        leilaoBLL.colocarDataFimLeilao(idLeilao, dataFim);
    }

    private ResultadoOperacao verificarSaldoEAtualizar(int idCliente, double valor, int idTipoLeilao) {
        ResultadoOperacao resultado = new ResultadoOperacao();
        Utilizador utilizador = utilizadorBLL.procurarUtilizadorPorId(idCliente);

        if (utilizador.getSaldo() < valor) {
            resultado.Sucesso = false;
            resultado.msgErro = "Saldo insuficiente.";
            return resultado;
        }

        double novoSaldo = utilizador.getSaldo() - valor;
        utilizador.setSaldo(novoSaldo);

        List<Utilizador> utilizadores = Tools.utilizadores;
        for (int i = 0; i < utilizadores.size(); i++) {
            if (utilizadores.get(i).getId() == idCliente) {
                utilizadores.set(i, utilizador);
                break;
            }
        }
        Transacao transacao = obterTipoEEstadoPorLeilao(idCliente, novoSaldo, valor, idTipoLeilao);

        transacaoBLL.criarTransacao(transacao);

        utilizadorDAL.gravarUtilizadores(utilizadores);

        resultado.Sucesso = true;
        resultado.msgErro = null;
        return resultado;
    }

    private Transacao obterTipoEEstadoPorLeilao(int idCliente, double saldo, double valorTransacao, int tipoLeilao) {
        int idEstadoTransacao = 0;
        int idTipoTransacao = 0;
        switch (tipoLeilao) {
            case Constantes.tiposLeilao.VENDA_DIRETA, Constantes.tiposLeilao.ELETRONICO,
                 Constantes.tiposLeilao.CARTA_FECHADA:
                idTipoTransacao = Constantes.tiposTransacao.LANCE_DEBITO;
                idEstadoTransacao = Constantes.estadosTransacao.ACEITE;
                break;
            default:
                break;
        }
        return new Transacao(0, idCliente, saldo, valorTransacao, LocalDateTime.now(), idTipoTransacao, idEstadoTransacao);
    }

    public int selecionarLanceVencedor(int idLeilao) {
        List<Lance> lances = obterLancesPorLeilao(idLeilao);

        if (lances.isEmpty()) {
            return 0;
        }

        Lance lanceVencedor = lances.get(0);

        for (Lance lance : lances) {
            if (lance.getValorLance() > lanceVencedor.getValorLance()) {
                lanceVencedor = lance;
            }
        }

        return lanceVencedor.getIdLance();
    }

    public String obterNomeVencedor(int idLance) {
        for (Lance lance : lances) {
            if (lance.getIdLance() == idLance) {
                Utilizador utilizadorVencedor = utilizadorBLL.procurarUtilizadorPorId(lance.getIdCliente());
                return utilizadorVencedor.getNomeUtilizador();
            }
        }
        return null;
    }


}


