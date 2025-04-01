package BLL;

import Model.Leilao;

import java.util.ArrayList;
import java.util.List;

public class EstatisticaBLL {

    private static List<Leilao> filtrarLeiloesFechados() {
        List<Leilao> todos = LeilaoBLL.listarLeiloes();
        List<Leilao> fechados = new ArrayList<>();

        for (Leilao l : todos) {
            if (l.getEstado() != null && l.getEstado().equalsIgnoreCase("Fechado")) {
                fechados.add(l);
            }
        }

        return fechados;
    }

    private static List<Leilao> filtrarLeiloesFechadosPorTipo(String tipoLeilao) {
        List<Leilao> fechados = filtrarLeiloesFechados();
        List<Leilao> resultado = new ArrayList<>();

        for (Leilao l : fechados) {
            if (l.getTipoLeilao() != null && l.getTipoLeilao().equalsIgnoreCase(tipoLeilao)) {
                resultado.add(l);
            }
        }

        return resultado;
    }

    public static int contarLeilaoFechados() {
        return filtrarLeiloesFechados().size();
    }

    public static int contarLeiloesFechadosPorTipo(String tipoLeilao) {
        return filtrarLeiloesFechadosPorTipo(tipoLeilao).size();
    }

    public static List<String> obterLeiloesFechadosFormatados() {
        List<Leilao> fechados = filtrarLeiloesFechados();
        List<String> resultado = new ArrayList<>();

        for (Leilao l : fechados) {
            resultado.add("ID: " + l.getId() +
                    " | Descrição: " + l.getDescricao() +
                    " | Tipo: " + l.getTipoLeilao());
        }

        return resultado;
    }

    public static List<String> listarLeiloesFechadosFormatadosPorTipo(String tipoLeilao) {
        List<Leilao> leiloes = filtrarLeiloesFechadosPorTipo(tipoLeilao);
        List<String> resultado = new ArrayList<>();

        for (Leilao l : leiloes) {
            String linha = "ID: " + l.getId()
                    + " | Descrição: " + l.getDescricao()
                    + " | Data Início: " + (l.getDataInicio() != null ? l.getDataInicio() : "N/D")
                    + " | Data Fim: " + (l.getDataFim() != null ? l.getDataFim() : "N/D");
            resultado.add(linha);
        }

        return resultado;
    }
}
