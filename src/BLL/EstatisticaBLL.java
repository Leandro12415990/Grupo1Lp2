package BLL;

import Model.Leilao;
import Utils.Constantes;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static BLL.ImportBll.obterTodosLeiloes;

public class EstatisticaBLL {

    private static List<Leilao> filtrarLeiloesFechados() {
        List<Leilao> todos = LeilaoBLL.listarLeiloes(false);
        List<Leilao> fechados = new ArrayList<>();

        for (Leilao l : todos) {
            if (l.getEstado() == Constantes.estadosLeilao.FECHADO) {
                fechados.add(l);
            }
        }

        return fechados;
    }

    private static List<Leilao> filtrarLeiloesFechadosPorTipo(int idTipoLeilao) {
        List<Leilao> resultado = new ArrayList<>();
        List<Leilao> fechados = filtrarLeiloesFechados();

        if (fechados == null) return resultado;

        String tipoStr = String.valueOf(idTipoLeilao);

        for (Leilao l : fechados) {
            String tipo = String.valueOf(l.getTipoLeilao());

            if (tipo != null && tipo.equals(tipoStr)) {
                resultado.add(l);
            }
        }

        return resultado;
    }


    public static int contarLeilaoFechados() {
        return filtrarLeiloesFechados().size();
    }

    public static int contarLeiloesFechadosPorTipo(int idTipoLeilao) {
        return filtrarLeiloesFechadosPorTipo(idTipoLeilao).size();
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

    public static List<String> listarLeiloesFechadosFormatadosPorTipo(int idTipoLeilao) {
        List<Leilao> leiloes = filtrarLeiloesFechadosPorTipo(idTipoLeilao);
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

        public static Object[] obterDadosLeilaoMaisTempoAtivo() {
            List<Leilao> leiloes = LeilaoBLL.carregarLeiloes();
            if (leiloes == null || leiloes.isEmpty()) return null;

            Leilao leilaoMaisTempo = null;
            Period maiorPeriodo = Period.ZERO;

            for (Leilao leilao : leiloes) {
                LocalDate dataInicio = leilao.getDataInicio();
                LocalDate dataFim = leilao.getDataFim();

                if (dataInicio == null || dataFim == null) {
                    continue;
                }

                Period periodo = Period.between(dataInicio, dataFim);

                if (MaiorPeriodo(periodo, maiorPeriodo)) {
                    maiorPeriodo = periodo;
                    leilaoMaisTempo = leilao;
                }
            }

            if (leilaoMaisTempo == null) return null;

            return new Object[] {
                    leilaoMaisTempo.getId(),
                    leilaoMaisTempo.getDescricao(),
                    maiorPeriodo.getYears(),
                    maiorPeriodo.getMonths(),
                    maiorPeriodo.getDays()
            };
        }


    private static boolean MaiorPeriodo(Period novo, Period atual) {
        if (novo.getYears() > atual.getYears()) return true;
        if (novo.getYears() == atual.getYears() && novo.getMonths() > atual.getMonths()) return true;
        if (novo.getYears() == atual.getYears() &&
                novo.getMonths() == atual.getMonths() &&
                novo.getDays() > atual.getDays()) return true;

        return false;
    }

}
