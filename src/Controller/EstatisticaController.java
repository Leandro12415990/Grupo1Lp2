package Controller;

import BLL.EstatisticaBLL;
import Model.ResultadoOperacao;

import java.util.List;

public class EstatisticaController {
    public static int contarLeilaoGlobal() {
        return EstatisticaBLL.contarLeilaoFechados();
    }

    public static int contarLeiloesFechadosPorTipo(String tipo) {
        return EstatisticaBLL.contarLeiloesFechadosPorTipo(tipo);
    }

    public static List<String> listarLeiloesFechadosFormatados() {
        return EstatisticaBLL.obterLeiloesFechadosFormatados();
    }

    public static List<String> listarLeiloesFechadosFormatadosPorTipo(String tipo) {
        return EstatisticaBLL.listarLeiloesFechadosFormatadosPorTipo(tipo);
    }
}
