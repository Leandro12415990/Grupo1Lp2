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
    }

    public int contarLeiloesFechadosPorTipo(int idTipo) {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.contarLeiloesFechadosPorTipo(idTipo);
    }

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
        return EstatisticaBLL.getDadosLeilaoComMaisLancesPorTipo(idTipo);
    }

    public double calcularMediaTempoEntreLances() {
        return EstatisticaBLL.calcularMediaTempoEntreLancesGeral();
    }

    public double calcularMediaTempoEntreLancesPorTipo(int tipo) {
        return EstatisticaBLL.calcularMediaTempoEntreLancesPorTipo(tipo);
    }

    public List<Leilao> getLeiloesSemLances() {
        return EstatisticaBLL.obterLeiloesSemLances();
    }

    public List<Leilao> getLeiloesSemLancesPorTipo(int idTipoLeilao) {
        return EstatisticaBLL.obterLeiloesSemLancesPorTipo(idTipoLeilao);
    }

    public double getMediaIdadeUtilizadores() {
        return EstatisticaBLL.calcularMediaIdadeUtilizadores();
    }

    public String[] getDominioMaisUsadoEPercentagem() {
        return EstatisticaBLL.calcularDominioMaisUsadoEPercentagem();
    }

    public List<String> getClientesOrdenadosPorValorMaisAlto(int idLeilao) {
        return EstatisticaBLL.listarClientesOrdenadosPorMaiorLance(idLeilao);
    }

    public Period getTempoAtivoLeilao(int idLeilao) {
        Leilao leilao = LeilaoBLL.procurarLeilaoPorId(idLeilao);
        return EstatisticaBLL.calcularTempoAtivoLeilao(leilao);
    }







}
