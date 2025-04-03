package Controller;

import BLL.EstatisticaBLL;
import Model.Leilao;
import Model.ResultadoOperacao;

import java.time.Period;
import java.util.List;

public class EstatisticaController {
    public static int contarLeilaoGlobal() {
        return EstatisticaBLL.contarLeilaoFechados();
    }

    public static int contarLeiloesFechadosPorTipo(int idTipo) {
        return EstatisticaBLL.contarLeiloesFechadosPorTipo(idTipo);
    }

    public static List<String> listarLeiloesFechadosFormatados() {
        return EstatisticaBLL.obterLeiloesFechadosFormatados();
    }

   /** public static List<String> listarLeiloesFechadosFormatadosPorTipo(int idTipo) {
        return EstatisticaBLL.listarLeiloesFechadosFormatadosPorTipo(idTipo);
    } */

    public static Leilao getLeilaoMaisTempoAtivo() {
        return EstatisticaBLL.obterLeilaoMaisTempoAtivo();
    }

    public static Leilao getLeilaoTipoMaisTempoAtivo(int idTipo) {
        return EstatisticaBLL.obterLeilaoTipoMaisTempoAtivo(idTipo);
    }

    public static String[] getDadosLeilaoComMaisLances() {
        return EstatisticaBLL.getDadosLeilaoComMaisLances();
    }

    public static String[] getDadosLeilaoComMaisLancesPorTipo(int idTipo) {
        return EstatisticaBLL.getDadosLeilaoComMaisLancesPorTipo(idTipo);
    }

    public static double calcularMediaTempoEntreLances() {
        return EstatisticaBLL.calcularMediaTempoEntreLancesEmMinutos();
    }

    public static double calcularMediaTempoEntreLancesPorTipo(int tipo) {
        return EstatisticaBLL.calcularMediaTempoEntreLancesPorTipo(tipo);
    }

    public static List<Leilao> getLeiloesSemLances() {
        return EstatisticaBLL.obterLeiloesSemLances();
    }

    public static List<Leilao> getLeiloesSemLancesPorTipo(int idTipoLeilao) {
        return EstatisticaBLL.obterLeiloesSemLancesPorTipo(idTipoLeilao);
    }





}
