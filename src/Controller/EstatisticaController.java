package Controller;

import BLL.EstatisticaBLL;
import BLL.LeilaoBLL;
import Model.Leilao;
import java.time.Period;
import java.util.List;

public class EstatisticaController {
    public int contarLeilaoGlobal() {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.contarLeilaoFechados();

    public int contarLeiloesFechadosPorTipo(int idTipo) {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.contarLeiloesFechadosPorTipo(idTipo);

    public List<String> listarLeiloesFechadosFormatados() {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.obterLeiloesFechadosFormatados();
    }

    public List<String> listarLeiloesFechadosFormatadosPorTipo(int idTipo) {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.listarLeiloesFechadosFormatadosPorTipo(idTipo);
    }

    public Leilao getLeilaoMaisTempoAtivo() {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.obterLeilaoMaisTempoAtivo();
    }

    public Leilao getLeilaoTipoMaisTempoAtivo(int idTipo) {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.obterLeilaoTipoMaisTempoAtivo(idTipo);
    }

    public String[] getDadosLeilaoComMaisLances() {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.getDadosLeilaoComMaisLances();
    }

    public String[] getDadosLeilaoComMaisLancesPorTipo(int idTipo) {
        return estatisticaBLL.obterLeilaoMaisTempoAtivo();
    }

    public double calcularMediaTempoEntreLances() {
        return estatisticaBLL.obterLeilaoTipoMaisTempoAtivo(idTipo);
    }

    public double calcularMediaTempoEntreLancesPorTipo(int tipo) {
        return estatisticaBLL.getDadosLeilaoComMaisLances();
    }

    public List<Leilao> getLeiloesSemLances() {
        return estatisticaBLL.getDadosLeilaoComMaisLancesPorTipo(idTipo);
    }

    public List<Leilao> getLeiloesSemLancesPorTipo(int idTipoLeilao) {
        return estatisticaBLL.calcularMediaTempoEntreLancesGeral();
    }

    public double getMediaIdadeUtilizadores() {
        return estatisticaBLL.calcularMediaTempoEntreLancesPorTipo(tipo);
    }

    public String[] getDominioMaisUsadoEPercentagem() {
        return estatisticaBLL.obterLeiloesSemLances();
    }

    public List<String> getClientesOrdenadosPorValorMaisAlto(int idLeilao) {
        return estatisticaBLL.obterLeiloesSemLancesPorTipo(idTipoLeilao);
    }

    public Period getTempoAtivoLeilao(int idLeilao) {
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
