package Controller;

import BLL.EstatisticaBLL;
import Model.ResultadoOperacao;

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

    public static List<String> listarLeiloesFechadosFormatadosPorTipo(int idTipo) {
        return EstatisticaBLL.listarLeiloesFechadosFormatadosPorTipo(idTipo);
    }
}
