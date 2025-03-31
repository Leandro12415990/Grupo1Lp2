package BLL;

import Model.Leilao;
import DAL.ImportDal;
import Utils.Constantes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LeilaoBLL {
    private static List<Leilao> leiloes = new ArrayList<>();

    public static List<Leilao> carregarLeiloes() {
        leiloes = ImportDal.carregarLeilao();
        int idEstado;
        for (Leilao leilao : leiloes) {
            idEstado = determinarEstadoByDatas(leilao.getDataInicio(), leilao.getDataFim(), leilao.getEstado());
            leilao.setEstado(idEstado);
            ImportDal.gravarLeilao(leiloes);
        }
        return leiloes;
    }

    public static void adicionarLeilao(Leilao leilao) {
        carregarLeiloes();
        leilao.setId(verificarUltimoId(leiloes) + 1);
        leiloes.add(leilao);
        ImportDal.gravarLeilao(leiloes);
    }

    private static int verificarUltimoId(List<Leilao> leiloes) {
        int ultimoId = 0;
        for (Leilao leilao : leiloes) {
            if (leilao.getId() > ultimoId) {
                ultimoId = leilao.getId();
            }
        }
        return ultimoId;
    }

    public static List<Leilao> listarLeiloes(boolean apenasDisponiveis) {
        carregarLeiloes();
        if (!apenasDisponiveis) {
            return leiloes;
        }
        List<Leilao> leiloesAtivos = new ArrayList<>();
        for (Leilao leilao : leiloes) {
            if (leilao.getEstado() == Constantes.EstadoAtivoLeilao) {
                leiloesAtivos.add(leilao);
            }
        }
        return leiloesAtivos;
    }

    public static Leilao procurarLeilaoPorId(int Id) {
        carregarLeiloes();
        for (Leilao leilao : leiloes) {
            if (leilao.getId() == Id) {
                return leilao;
            }
        }
        return null;
    }

    public static void eliminarLeilao(Leilao leilao) {
        leiloes.remove(leilao);
        ImportDal.gravarLeilao(leiloes);
    }

    public static boolean editarLeilao(int id, int idProduto, String descricao, String tipoLeilao, LocalDate dataInicio, LocalDate dataFim, double valorMin, double valorMax, double multiploLance, int idEstado) {
        Leilao leilao = procurarLeilaoPorId(id);
        if (leilao != null) {
            leilao.setIdProduto(idProduto);
            leilao.setDescricao(descricao);
            leilao.setTipoLeilao(tipoLeilao);
            leilao.setDataInicio(dataInicio);
            leilao.setDataFim(dataFim);
            leilao.setValorMinimo(valorMin);
            leilao.setValorMaximo(valorMax);
            leilao.setMultiploLance(multiploLance);
            leilao.setEstado(idEstado);
            ImportDal.gravarLeilao(leiloes);
            return true;
        }
        return false;
    }

    public static int determinarEstadoByDatas(LocalDate dataInicio, LocalDate dataFim, int idEstado) {
        if (idEstado != Constantes.EstadoInativoLeilao || idEstado != Constantes.EstadoCanceladoLeilao) {
            if (dataFim != null) {
                if (dataFim.isBefore(LocalDate.now())) {
                    return Constantes.EstadoFechadoLeilao;
                }
            }
            if (dataInicio.isBefore(LocalDate.now()) || dataInicio.equals(LocalDate.now())) {
                return Constantes.EstadoAtivoLeilao;
            }
            if (dataInicio.isAfter(LocalDate.now())) {
                return Constantes.EstadoPendenteLeilao;
            }
            return Constantes.EstadoPendenteLeilao;
        }
        return idEstado;
    }

}
