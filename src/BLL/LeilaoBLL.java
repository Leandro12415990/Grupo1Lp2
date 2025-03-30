package BLL;

import DAL.ProdutoDal;
import Model.Leilao;
import DAL.ImportDal;
import Model.Produto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LeilaoBLL {
    static final int EstadoAtivoLeilao = 1;
    static final int EstadoPendenteLeilao = 2;
    static final int EstadoCanceladoLeilao = 3;
    static final int EstadoFechadoLeilao = 4;
    static final int EstadoInativoLeilao = 5;
    private static List<Leilao> leiloes = new ArrayList<>();

    public static List<Leilao> carregarLeiloes() {
        leiloes = ImportDal.carregarLeilao();
        for (Leilao leilao : leiloes) {
            atualizarEstadoLeilaoAutomaticamente(leilao);
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
            if (leilao.getEstado() == EstadoAtivoLeilao) {
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

    public static void atualizarEstadoLeilaoAutomaticamente(Leilao leilao) {
        if (leilao.getEstado() != EstadoInativoLeilao || leilao.getEstado() != EstadoCanceladoLeilao) {
            if (leilao.getDataFim() != null) {
                if (leilao.getDataFim().isBefore(LocalDate.now())) {
                    leilao.setEstado(EstadoFechadoLeilao);
                    ImportDal.gravarLeilao(leiloes);
                    return;
                }
            }
            if (leilao.getDataInicio().isBefore(LocalDate.now()) || leilao.getDataInicio().equals(LocalDate.now())) {
                leilao.setEstado(EstadoAtivoLeilao);
                ImportDal.gravarLeilao(leiloes);
                return;
            }
            if (leilao.getDataInicio().isAfter(LocalDate.now())) {
                leilao.setEstado(EstadoPendenteLeilao);
                ImportDal.gravarLeilao(leiloes);
            }
        }
    }
}
