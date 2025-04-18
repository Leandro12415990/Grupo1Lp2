package Controller;

import BLL.EstatisticaBLL;
import BLL.LeilaoBLL;
import Model.Leilao;
import java.time.Period;
import java.util.List;

public class EstatisticaController {

    private final EstatisticaBLL estatisticaBLL;
    private final LeilaoBLL leilaoBLL;

    public EstatisticaController(EstatisticaBLL estatisticaBLL, LeilaoBLL leilaoBLL) {
        this.estatisticaBLL = estatisticaBLL;
        this.leilaoBLL = leilaoBLL;
    }

    public int contarLeilaoGlobal() {
        return estatisticaBLL.contarLeilaoFechados();
    }

    public int contarLeiloesFechadosPorTipo(int idTipo) {
        return estatisticaBLL.contarLeiloesFechadosPorTipo(idTipo);
    }

    public List<String> listarLeiloesFechadosFormatados() {
        return estatisticaBLL.obterLeiloesFechadosFormatados();
    }

    public List<String> listarLeiloesFechadosFormatadosPorTipo(int idTipo) {
        return estatisticaBLL.listarLeiloesFechadosFormatadosPorTipo(idTipo);
    }

    public Leilao getLeilaoMaisTempoAtivo() {
        return estatisticaBLL.obterLeilaoMaisTempoAtivo();
    }

    public Leilao getLeilaoTipoMaisTempoAtivo(int idTipo) {
        return estatisticaBLL.obterLeilaoTipoMaisTempoAtivo(idTipo);
    }

    public String[] getDadosLeilaoComMaisLances() {
        return estatisticaBLL.getDadosLeilaoComMaisLances();
    }

    public String[] getDadosLeilaoComMaisLancesPorTipo(int idTipo) {
        return estatisticaBLL.getDadosLeilaoComMaisLancesPorTipo(idTipo);
    }

    public double calcularMediaTempoEntreLances() {
        return estatisticaBLL.calcularMediaTempoEntreLancesGeral();
    }

    public double calcularMediaTempoEntreLancesPorTipo(int tipo) {
        return estatisticaBLL.calcularMediaTempoEntreLancesPorTipo(tipo);
    }

    public List<Leilao> getLeiloesSemLances() {
        return estatisticaBLL.obterLeiloesSemLances();
    }

    public List<Leilao> getLeiloesSemLancesPorTipo(int idTipoLeilao) {
        return estatisticaBLL.obterLeiloesSemLancesPorTipo(idTipoLeilao);
    }

    public double getMediaIdadeUtilizadores() {
        return estatisticaBLL.calcularMediaIdadeUtilizadores();
    }

    public String[] getDominioMaisUsadoEPercentagem() {
        return estatisticaBLL.calcularDominioMaisUsadoEPercentagem();
    }

    public List<String> getClientesOrdenadosPorValorMaisAlto(int idLeilao) {
        return estatisticaBLL.listarClientesOrdenadosPorMaiorLance(idLeilao);
    }

    public Period getTempoAtivoLeilao(int idLeilao) {
        Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);
        return estatisticaBLL.calcularTempoAtivoLeilao(leilao);
    }
}
