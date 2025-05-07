package BLL;

import DAL.LanceDAL;
import DAL.UtilizadorDAL;
import Model.*;
import Utils.Constantes;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LanceBLL {
    private static final List<Lance> lances = new ArrayList<>();

    public ResultadoOperacao adicionarLanceDireto(int idLeilao, double valorLance, int idCliente, int idTipoLeilao) throws MessagingException, IOException {
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        LanceDAL lanceDAL = new LanceDAL();
        TransacaoBLL transacaoBLL = new TransacaoBLL();
        ResultadoOperacao resultado = new ResultadoOperacao();

        ResultadoOperacao saldoVerificado = verificarSaldo(idCliente, valorLance);
        if (!saldoVerificado.Sucesso) {
            return saldoVerificado;
        }

        int idLance = verUltimoId() + 1;
        Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);
        transacaoBLL.atualizarSaldo(idCliente,valorLance,'-',false,true);

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

    public ResultadoOperacao adicionarLanceCartaFechada(int idLeilao, double valorLance, int idCliente, int idTipoLeilao) throws MessagingException, IOException {
        LanceDAL lanceDAL = new LanceDAL();
        TransacaoBLL transacaoBLL = new TransacaoBLL();
        ResultadoOperacao resultado = new ResultadoOperacao();

        ResultadoOperacao saldoVerificado = verificarSaldo(idCliente, valorLance);
        if (!saldoVerificado.Sucesso) {
            return saldoVerificado;
        }

        int idLance = verUltimoId() + 1;
        transacaoBLL.atualizarSaldo(idCliente,valorLance,'-',false,true);
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

    public ResultadoOperacao adicionarLanceEletronico(int idLeilao, double novoValorLance, int idCliente, int idTipoLeilao) throws MessagingException, IOException {
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        LanceDAL lanceDAL = new LanceDAL();
        ResultadoOperacao resultado = new ResultadoOperacao();
        TransacaoBLL transacaoBLL = new TransacaoBLL();

        Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);
        double multiploLanceIncremento = leilao.getValorMaximo();
        double valorMinimo = leilao.getValorMinimo();


        List<Lance> lancesDoLeilao = obterLancesPorLeilao(idLeilao);
        double ultimoLance;

        if (lancesDoLeilao.isEmpty()) {
            ultimoLance = valorMinimo;
        } else {
            ultimoLance = lancesDoLeilao.get(lancesDoLeilao.size() -1).getValorLance();
        }

        double valorEsperado = ultimoLance + multiploLanceIncremento;

        if (Double.compare(novoValorLance, valorEsperado) != 0) {
            resultado.Sucesso = false;
            resultado.msgErro = String.format("O valor do lance deve ser exatamente %.2f (Último lance + Múltiplo)", valorEsperado);
            return resultado;
        }

        ResultadoOperacao saldoVerificado = verificarSaldo(idCliente, novoValorLance);
        if (!saldoVerificado.Sucesso) {
            return saldoVerificado;
        }
        transacaoBLL.reembolsarUltimoLanceEletronico(idLeilao);
        transacaoBLL.atualizarSaldo(idCliente,novoValorLance,'-',false,true);

        carregarLances();
        int idLance = verUltimoId() + 1;

        LocalDateTime dataLance = LocalDateTime.now();
        int pontosUtilizados = 0;

        Lance novoLance = new Lance(idLance, idLeilao, idCliente, novoValorLance, lancesDoLeilao.size() + 1, pontosUtilizados, dataLance);

        lances.add(novoLance);
        lanceDAL.gravarLances(lances);

        leilaoBLL.atualizarLeilao(leilao);

        resultado.Sucesso = true;
        resultado.Objeto = novoLance;
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
    carregarLances();
	List<Lance> lancesByLeilao = new ArrayList<>();
        for (Lance lance : lances) {
            if (idLeilao == 0 || lance.getIdLeilao() == idLeilao) {
                lancesByLeilao.add(lance);
            }
        }
        return lancesByLeilao;
    }

    public void fimLeilao(int idLeilao, LocalDateTime dataFim) {
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        leilaoBLL.colocarDataFimLeilao(idLeilao, dataFim);
    }

    private ResultadoOperacao verificarSaldo(int idCliente, double valor) throws MessagingException, IOException {
        ResultadoOperacao resultado = new ResultadoOperacao();
        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        Utilizador utilizador = utilizadorBLL.procurarUtilizadorPorId(idCliente);


        if (utilizador.getSaldo() < valor) {
            resultado.Sucesso = false;
            resultado.msgErro = "Saldo insuficiente.";
            return resultado;
        } else {
            resultado.Sucesso = true;
        }
        return resultado;
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

    public Utilizador obterVencedor(int idLance) {
        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        for (Lance lance : lances) {
            if (lance.getIdLance() == idLance) {
                return utilizadorBLL.procurarUtilizadorPorId(lance.getIdCliente());
            }
        }
        return null;
    }

    public void carregarLances() {
        LanceDAL lanceDAL = new LanceDAL();
        List<Lance> lancesCarregados = lanceDAL.carregarLances();
        lances.clear();
        lances.addAll(lancesCarregados);
    }

}


