package BLL;

import Model.Lance;
import Model.Leilao;
import Utils.Constantes;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class EstatisticaBLL {
    /** Filtragem dos Leilões */
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
        List<Leilao> fechados = filtrarLeiloesFechados();
        List<Leilao> resultado = new ArrayList<>();

        for (Leilao l : fechados) {
            if (l.getTipoLeilao() == idTipoLeilao) {
                resultado.add(l);
            }
        }

        return resultado;
    }

    /** Contagem dos leilões fechados */
    public static int contarLeilaoFechados() {
        return filtrarLeiloesFechados().size();
    }

    public static int contarLeiloesFechadosPorTipo(int idTipoLeilao) {
        return filtrarLeiloesFechadosPorTipo(idTipoLeilao).size();
    }

    /** Listagem de leilões por tipo */

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
/**
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
*/
    /** Obter o leilão mais tempo ativo */

    public static Leilao obterLeilaoTipoMaisTempoAtivo(int idTipoLeilao) {
        List<Leilao> leiloes = LeilaoBLL.carregarLeiloes();
        if (leiloes == null || leiloes.isEmpty()) return null;

        Leilao leilaoMaisTempo = null;
        Period maiorPeriodo = Period.ZERO;

        for (Leilao leilao : leiloes) {
            if (leilao.getTipoLeilao() != idTipoLeilao) continue;

            if (leilao.getDataInicio() == null || leilao.getDataFim() == null) continue;

            Period periodo = Period.between(leilao.getDataInicio(), leilao.getDataFim());

            if (isMaiorPeriodo(periodo, maiorPeriodo)) {
                maiorPeriodo = periodo;
                leilaoMaisTempo = leilao;
            }
        }

        return leilaoMaisTempo;
    }

    public static Leilao obterLeilaoMaisTempoAtivo() {
        List<Leilao> leiloes = LeilaoBLL.carregarLeiloes();
        if (leiloes == null || leiloes.isEmpty()) return null;

        Leilao leilaoMaisTempo = null;
        Period maiorPeriodo = Period.ZERO;

        for (Leilao leilao : leiloes) {
            if (leilao.getDataInicio() == null || leilao.getDataFim() == null) continue;

            Period periodo = Period.between(leilao.getDataInicio(), leilao.getDataFim());

            if (isMaiorPeriodo(periodo, maiorPeriodo)) {
                maiorPeriodo = periodo;
                leilaoMaisTempo = leilao;
            }
        }

        return leilaoMaisTempo;
    }

    private static boolean isMaiorPeriodo(Period novo, Period atual) {
        if (novo.getYears() > atual.getYears()) return true;
        if (novo.getYears() == atual.getYears() && novo.getMonths() > atual.getMonths()) return true;
        return novo.getYears() == atual.getYears()
                && novo.getMonths() == atual.getMonths()
                && novo.getDays() > atual.getDays();
    }

    /** Obter o leilão com mais lances feitos */

    public static int obterIdLeilaoComMaisLances() {
        List<Lance> lances = LanceBLL.carregarLance();
        if (lances == null || lances.isEmpty()) return -1;

        List<Integer> idsVerificados = new ArrayList<>();
        int idLeilaoMaisLances = -1;
        int maxLances = 0;

        for (Lance lance : lances) {
            int idAtual = lance.getIdLeilao();

            if (idsVerificados.contains(idAtual)) continue;
            idsVerificados.add(idAtual);

            int contador = 0;
            for (Lance outro : lances) {
                if (outro.getIdLeilao() == idAtual) {
                    contador++;
                }
            }

            if (contador > maxLances) {
                maxLances = contador;
                idLeilaoMaisLances = idAtual;
            }
        }

        return idLeilaoMaisLances;
    }

    public static int obterIdLeilaoComMaisTempoTipo(int idTipoLeilao) {
        List<Lance> lances = LanceBLL.carregarLance();

    }

}
