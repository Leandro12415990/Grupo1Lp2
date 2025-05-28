package BLL;

import Controller.LeilaoController;
import DAL.LanceDAL;
import DAL.NegociacaoDAL;
import Model.*;
import Utils.Constantes;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LanceBLL {
    private static final List<Lance> lances = new ArrayList<>();
    private static final Object lockLances = new Object();


    public ResultadoOperacao adicionarLanceDireto(int idLeilao, double valorLance, int idCliente, int idTipoLeilao) throws MessagingException, IOException {
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        LeilaoController leilaoController = new LeilaoController();
        LanceDAL lanceDAL = new LanceDAL();
        TransacaoBLL transacaoBLL = new TransacaoBLL();
        ResultadoOperacao resultado = new ResultadoOperacao();

        ResultadoOperacao saldoVerificado = verificarSaldo(idCliente, valorLance);
        if (!saldoVerificado.Sucesso) return saldoVerificado;

        int idLance = verUltimoId() + 1;
        Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);
        transacaoBLL.atualizarSaldo(idCliente, valorLance, '-', false, true);

        int numLance = 0;
        int pontosUtilizados = 0;
        LocalDateTime dataLance = LocalDateTime.now();
        int estado = Constantes.estadosLance.DEFAULT;
        Lance lance = new Lance(idLance, idLeilao, idCliente, valorLance, numLance, pontosUtilizados, dataLance, estado);
        lances.add(lance);
        lanceDAL.gravarLances(lances);

        if (valorLance == leilao.getValorMinimo()) leilaoController.fecharLeilao(idLeilao, dataLance);

        resultado.Sucesso = true;
        resultado.Objeto = lance;
        return resultado;
    }

    public ResultadoOperacao adicionarLanceCartaFechada(int idLeilao, double valorLance, int idCliente, int idTipoLeilao) throws MessagingException, IOException {
        LanceDAL lanceDAL = new LanceDAL();
        TransacaoBLL transacaoBLL = new TransacaoBLL();
        ResultadoOperacao resultado = new ResultadoOperacao();

        ResultadoOperacao saldoVerificado = verificarSaldo(idCliente, valorLance);
        if (!saldoVerificado.Sucesso) return saldoVerificado;

        int idLance = verUltimoId() + 1;
        transacaoBLL.atualizarSaldo(idCliente, valorLance, '-', false, true);
        int numLance = 0;
        int pontosUtilizados = 0;
        LocalDateTime dataLance = LocalDateTime.now();
        int estado = Constantes.estadosLance.DEFAULT;

        Lance lance = new Lance(idLance, idLeilao, idCliente, valorLance, numLance, pontosUtilizados, dataLance, estado);
        lances.add(lance);
        lanceDAL.gravarLances(lances);

        resultado.Sucesso = true;
        resultado.Objeto = lance;
        return resultado;
    }

    public ResultadoOperacao adicionarLanceEletronico(int idLeilao, double novoValorLance, int idCliente, int idTipoLeilao) throws MessagingException, IOException {
        ResultadoOperacao resultado = new ResultadoOperacao();

        LanceDAL lanceDAL = new LanceDAL();
        TransacaoBLL transacaoBLL = new TransacaoBLL();
        LeilaoBLL leilaoBLL = new LeilaoBLL();

        synchronized (lockLances) {
            Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);
            double multiploLanceIncremento = leilao.getMultiploLance();
            double valorMinimo = leilao.getValorMinimo();

            List<Lance> lancesDoLeilao = obterLancesPorLeilao(idLeilao);
            double ultimoLance = lancesDoLeilao.isEmpty() ? valorMinimo :
                    lancesDoLeilao.get(lancesDoLeilao.size() - 1).getValorLance();

            double valorEsperado = ultimoLance + multiploLanceIncremento;
            if (Double.compare(novoValorLance, valorEsperado) != 0) {
                resultado.Sucesso = false;
                resultado.msgErro = String.format("O valor do lance deve ser exatamente %.2f (Último lance + Múltiplo)", valorEsperado);
                return resultado;
            }

            ResultadoOperacao saldoVerificado = verificarSaldo(idCliente, novoValorLance);
            if (!saldoVerificado.Sucesso) return saldoVerificado;

            transacaoBLL.reembolsarUltimoLanceEletronico(idLeilao);
            transacaoBLL.atualizarSaldo(idCliente, novoValorLance, '-', false, true);

            // Gera novo ID de forma segura dentro do lock
            int idLance = verUltimoId() + 1;
            LocalDateTime dataLance = LocalDateTime.now();
            int pontosUtilizados = 0;
            int estado = Constantes.estadosLance.DEFAULT;

            Lance novoLance = new Lance(idLance, idLeilao, idCliente, novoValorLance, lancesDoLeilao.size() + 1, pontosUtilizados, dataLance, estado);
            lances.add(novoLance);
            lanceDAL.gravarLances(lances);

            boolean estendido = leilaoBLL.estenderFimLeilaoSeNecessario(leilao, dataLance);
            if (estendido) leilaoBLL.atualizarLeilao(leilao);

            resultado.Sucesso = true;
            resultado.Objeto = novoLance;
        }
        return resultado;
    }

    public int verUltimoId() {
        int ultimoId = 0;
        for (Lance lance : lances) {
            if (lance.getIdLance() > ultimoId) ultimoId = lance.getIdLance();
        }
        return ultimoId;
    }

    public List<Lance> listarMeuLance(int IdCliente) {
        List<Lance> lancesByCliente = new ArrayList<>();
        for (Lance lance : lances) {
            if (lance.getIdCliente() == IdCliente) lancesByCliente.add(lance);
        }
        return lancesByCliente;
    }

    public List<Lance> obterLancesPorLeilao(int idLeilao) {
        carregarLances();

        if (lances == null) return Collections.emptyList();

        List<Lance> lancesSafeCopy;
        synchronized (lances) {
            lancesSafeCopy = new ArrayList<>(lances);
        }

        List<Lance> lancesByLeilao = new ArrayList<>();
        for (Lance lance : lancesSafeCopy) {
            if (lance == null) continue;
            if (idLeilao == 0 || lance.getIdLeilao() == idLeilao) {
                lancesByLeilao.add(lance);
            }
        }

        return lancesByLeilao;
    }

    private ResultadoOperacao verificarSaldo(int idCliente, double valor) throws MessagingException, IOException {
        ResultadoOperacao resultado = new ResultadoOperacao();
        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        Utilizador utilizador = utilizadorBLL.procurarUtilizadorPorId(idCliente);

        if (utilizador == null) {
            System.err.printf("Utilizador não encontrado para o idCliente %d%n", idCliente);
            resultado.Sucesso = false;
            resultado.msgErro = "Utilizador não encontrado.";
            return resultado;
        }

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
        if (lances.isEmpty()) return 0;
        Lance lanceVencedor = lances.get(0);

        for (Lance lance : lances) {
            if (lance.getValorLance() > lanceVencedor.getValorLance()) lanceVencedor = lance;
        }
        return lanceVencedor.getIdLance();
    }

    public Utilizador obterVencedor(int idLance) {
        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        for (Lance lance : lances) {
            if (lance.getIdLance() == idLance) return utilizadorBLL.procurarUtilizadorPorId(lance.getIdCliente());
        }
        return null;
    }

    public List<Lance> carregarLances() {
        LanceDAL lanceDAL = new LanceDAL();
        List<Lance> lancesCarregados = lanceDAL.carregarLances();
        synchronized (lockLances) {
            lances.clear();
            lances.addAll(lancesCarregados);
        }
        return lancesCarregados;
    }

    public ResultadoOperacao fazerProposta(int idNegociacao, int idClienteProponente, double valorProposta) throws MessagingException, IOException {
        ResultadoOperacao resultado = new ResultadoOperacao();
        NegociacaoDAL negociacaoDAL = new NegociacaoDAL();
        List<Negociacao> negociacoes = negociacaoDAL.carregarNegociacoes();
        carregarLances();

        for (Negociacao n : negociacoes) {
            if (n.getIdNegociacao() == idNegociacao && n.getEstado() == 1) {
                if (n.getIdCliente() == idClienteProponente) {
                    resultado.msgErro = "Não podes dar lance no teu próprio leilão.";
                    return resultado;
                }

                if (valorProposta >= n.getValor()) {
                    n.setEstado(4);
                    resultado.Sucesso = true;
                    resultado.msgErro = "Lance aceito. Leilão fechado.";
                } else {

                    // Verifica saldo do cliente
                    ResultadoOperacao saldoVerificado = verificarSaldo(idClienteProponente, valorProposta);
                    if (!saldoVerificado.Sucesso) {
                        return saldoVerificado;
                    }

                    int idLance = verUltimoId() + 1;
                    LocalDateTime dataLance = LocalDateTime.now();
                    int estado = Constantes.estadosLance.PROPOSTA;

                    Lance novoLance = new Lance(idLance, 0, idClienteProponente, valorProposta, idNegociacao, 0, dataLance, estado);

                    synchronized (lockLances) {
                        lances.add(novoLance);
                        new LanceDAL().gravarLances(lances);
                    }

                    resultado.Sucesso = true;
                    resultado.msgErro = "Proposta enviada ao dono do leilão.";
                    resultado.Objeto = novoLance;

                    return resultado;
                }

            }
        } resultado.msgErro = "Leilão não encontrado ou já encerrado.";
        return resultado;
    }

}


