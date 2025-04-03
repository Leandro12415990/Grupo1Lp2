package BLL;

import Model.Lance;
import Model.Leilao;
import Utils.Constantes;

import java.time.Duration;
import java.time.LocalDateTime;
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

    public static String[] getDadosLeilaoComMaisLances() {
        List<Lance> lances = LanceBLL.carregarLance();
        if (lances == null || lances.isEmpty()) return null;

        List<Integer> idsVerificados = new ArrayList<>();
        int idLeilaoMaisLances = -1;
        int maxLances = 0;

        for (Lance l1 : lances) {
            int idAtual = l1.getIdLeilao();
            if (idsVerificados.contains(idAtual)) continue;
            idsVerificados.add(idAtual);

            int contador = 0;
            for (Lance l2 : lances) {
                if (l2.getIdLeilao() == idAtual) contador++;
            }

            if (contador > maxLances) {
                maxLances = contador;
                idLeilaoMaisLances = idAtual;
            }
        }

        if (idLeilaoMaisLances == -1) return null;

        Leilao leilao = LeilaoBLL.procurarLeilaoPorId(idLeilaoMaisLances);
        if (leilao == null) return null;

        return new String[] {
                String.valueOf(leilao.getId()),
                leilao.getDescricao(),
                String.valueOf(maxLances)
        };
    }

    public static String[] getDadosLeilaoComMaisLancesPorTipo(int idTipoLeilao) {
        List<Lance> lances = LanceBLL.carregarLance();
        List<Leilao> leiloes = LeilaoBLL.carregarLeiloes();

        if (lances == null || lances.isEmpty() || leiloes == null || leiloes.isEmpty()) {
            return null;
        }

        List<Integer> idsVerificados = new ArrayList<>();
        int idLeilaoMaisLances = -1;
        int maxLances = 0;
        String descricaoLeilao = "";

        for (Lance lance : lances) {
            int idAtual = lance.getIdLeilao();

            if (idsVerificados.contains(idAtual)) continue;
            idsVerificados.add(idAtual);

            Leilao leilao = null;
            for (Leilao l : leiloes) {
                if (l.getId() == idAtual) {
                    leilao = l;
                    break;
                }
            }

            if (leilao == null || leilao.getTipoLeilao() != idTipoLeilao) continue;

            int contador = 0;
            for (Lance outro : lances) {
                if (outro.getIdLeilao() == idAtual) {
                    contador++;
                }
            }

            if (contador > maxLances) {
                maxLances = contador;
                idLeilaoMaisLances = idAtual;
                descricaoLeilao = leilao.getDescricao();
            }
        }

        if (idLeilaoMaisLances == -1) return null;

        return new String[] {
                String.valueOf(idLeilaoMaisLances),
                descricaoLeilao,
                String.valueOf(maxLances)
        };
    }

    /** Calcular a media de tempo para acontecer um lance */

    public static double calcularMediaTempoEntreLancesEmMinutos() {
            List<Lance> lances = LanceBLL.carregarLance();
            if (lances == null || lances.isEmpty()) return -1;

            List<Integer> idsVerificados = new ArrayList<>();
            double somaMinutos = 0;
            int totalIntervalos = 0;

            for (Lance l : lances) {
                int idLeilao = l.getIdLeilao();
                if (idsVerificados.contains(idLeilao)) continue;
                idsVerificados.add(idLeilao);

                List<Lance> lancesDoLeilao = new ArrayList<>();
                for (Lance outro : lances) {
                    if (outro.getIdLeilao() == idLeilao) {
                        lancesDoLeilao.add(outro);
                    }
                }

                if (lancesDoLeilao.size() < 2) continue;

                for (int i = 0; i < lancesDoLeilao.size() - 1; i++) {
                    for (int j = i + 1; j < lancesDoLeilao.size(); j++) {
                        if (lancesDoLeilao.get(i).getDataLance().isAfter(lancesDoLeilao.get(j).getDataLance())) {
                            Lance temp = lancesDoLeilao.get(i);
                            lancesDoLeilao.set(i, lancesDoLeilao.get(j));
                            lancesDoLeilao.set(j, temp);
                        }
                    }
                }

                for (int i = 1; i < lancesDoLeilao.size(); i++) {
                    LocalDateTime anterior = lancesDoLeilao.get(i - 1).getDataLance();
                    LocalDateTime atual = lancesDoLeilao.get(i).getDataLance();
                    long minutos = Duration.between(anterior, atual).toMinutes();

                    somaMinutos += minutos;
                    totalIntervalos++;
                }
            }

            if (totalIntervalos == 0) return -1;

            return somaMinutos / totalIntervalos;
        }

    public static double calcularMediaTempoEntreLancesPorTipo(int idTipoLeilao) {
        List<Lance> lances = LanceBLL.carregarLance();
        List<Leilao> leiloes = LeilaoBLL.carregarLeiloes();

        if (lances == null || lances.isEmpty() || leiloes == null || leiloes.isEmpty()) return -1;

        List<Integer> idsVerificados = new ArrayList<>();
        double somaMinutos = 0;
        int totalIntervalos = 0;

        for (Leilao leilao : leiloes) {
            if (leilao.getTipoLeilao() != idTipoLeilao) continue;

            int idLeilao = leilao.getId();
            if (idsVerificados.contains(idLeilao)) continue;
            idsVerificados.add(idLeilao);

            List<Lance> lancesDoLeilao = new ArrayList<>();
            for (Lance l : lances) {
                if (l.getIdLeilao() == idLeilao) {
                    lancesDoLeilao.add(l);
                }
            }

            if (lancesDoLeilao.size() < 2) continue;

            for (int i = 0; i < lancesDoLeilao.size() - 1; i++) {
                for (int j = i + 1; j < lancesDoLeilao.size(); j++) {
                    if (lancesDoLeilao.get(i).getDataLance().isAfter(lancesDoLeilao.get(j).getDataLance())) {
                        Lance temp = lancesDoLeilao.get(i);
                        lancesDoLeilao.set(i, lancesDoLeilao.get(j));
                        lancesDoLeilao.set(j, temp);
                    }
                }
            }

            for (int i = 1; i < lancesDoLeilao.size(); i++) {
                LocalDateTime anterior = lancesDoLeilao.get(i - 1).getDataLance();
                LocalDateTime atual = lancesDoLeilao.get(i).getDataLance();
                long minutos = Duration.between(anterior, atual).toMinutes();

                somaMinutos += minutos;
                totalIntervalos++;
            }
        }

        if (totalIntervalos == 0) return -1;

        return somaMinutos / totalIntervalos;
    }

    /** Calcular a quantidade de leiloes sem lance */

    public static List<Leilao> obterLeiloesSemLances() {
        List<Leilao> leiloes = LeilaoBLL.carregarLeiloes();
        List<Lance> lances = LanceBLL.carregarLance();

        if (leiloes == null || leiloes.isEmpty()) return new ArrayList<>();

        List<Leilao> semLances = new ArrayList<>();

        for (Leilao leilao : leiloes) {
            boolean temLance = false;

            for (Lance lance : lances) {
                if (lance.getIdLeilao() == leilao.getId()) {
                    temLance = true;
                    break;
                }
            }

            if (!temLance) {
                semLances.add(leilao);
            }
        }

        return semLances;
    }

    public static List<Leilao> obterLeiloesSemLancesPorTipo(int idTipoLeilao) {
        List<Leilao> leiloes = LeilaoBLL.carregarLeiloes();
        List<Lance> lances = LanceBLL.carregarLance();

        if (leiloes == null || leiloes.isEmpty()) return new ArrayList<>();

        List<Leilao> semLances = new ArrayList<>();

        for (Leilao leilao : leiloes) {
            if (leilao.getTipoLeilao() != idTipoLeilao) continue;

            boolean temLance = false;

            for (Lance lance : lances) {
                if (lance.getIdLeilao() == leilao.getId()) {
                    temLance = true;
                    break;
                }
            }

            if (!temLance) {
                semLances.add(leilao);
            }
        }

        return semLances;
    }



}
