package Controller;

import BLL.EstatisticaBLL;
import BLL.LeilaoBLL;
import Model.Leilao;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.time.Period;
import java.util.List;

public class EstatisticaController {
    public int contarLeilaoGlobal() throws MessagingException, IOException {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.contarLeilaoFechados();
    }

    public int contarLeiloesFechadosPorTipo(int idTipo) throws MessagingException, IOException {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.contarLeiloesFechadosPorTipo(idTipo);
    }

    public List<String> listarLeiloesFechadosFormatados() throws MessagingException, IOException {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.obterLeiloesFechadosFormatados();
    }

    public List<String> listarLeiloesFechadosFormatadosPorTipo(int idTipo) throws MessagingException, IOException {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.listarLeiloesFechadosFormatadosPorTipo(idTipo);
    }

    public Leilao getLeilaoMaisTempoAtivo() throws MessagingException, IOException {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.obterLeilaoMaisTempoAtivo();
    }

    public Leilao getLeilaoTipoMaisTempoAtivo(int idTipo) throws MessagingException, IOException {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.obterLeilaoTipoMaisTempoAtivo(idTipo);
    }

    public String[] getDadosLeilaoComMaisLances() {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.getDadosLeilaoComMaisLances();
    }

    public String[] getDadosLeilaoComMaisLancesPorTipo(int idTipo) throws MessagingException, IOException {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.getDadosLeilaoComMaisLancesPorTipo(idTipo);
    }

    public double calcularMediaTempoEntreLances() {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.calcularMediaTempoEntreLancesGeral();
    }

    public double calcularMediaTempoEntreLancesPorTipo(int tipo) throws MessagingException, IOException {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.calcularMediaTempoEntreLancesPorTipo(tipo);
    }

    public List<Leilao> getLeiloesSemLances() throws MessagingException, IOException {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.obterLeiloesSemLances();
    }

    public List<Leilao> getLeiloesSemLancesPorTipo(int idTipoLeilao) throws MessagingException, IOException {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.obterLeiloesSemLancesPorTipo(idTipoLeilao);
    }

    public double getMediaIdadeUtilizadores() {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.calcularMediaIdadeUtilizadores();
    }

    public String[] getDominioMaisUsadoEPercentagem() {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.calcularDominioMaisUsadoEPercentagem();
    }

    public List<String> getClientesOrdenadosPorValorMaisAlto(int idLeilao) {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        return estatisticaBLL.listarClientesOrdenadosPorMaiorLance(idLeilao);
    }

    public Period getTempoAtivoLeilao(int idLeilao) {
        EstatisticaBLL estatisticaBLL = new EstatisticaBLL();
        LeilaoBLL leilaoBLL = new LeilaoBLL();
        Leilao leilao = leilaoBLL.procurarLeilaoPorId(idLeilao);
        return estatisticaBLL.calcularTempoAtivoLeilao(leilao);
    }
}
