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

    public static int getIdLeilaoComMaisLances() {
        return EstatisticaBLL.obterIdLeilaoComMaisLances();
    }




}
