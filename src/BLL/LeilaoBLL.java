package BLL;

import Model.Leilao;
import DAL.ImportDal;

import java.util.ArrayList;
import java.util.List;

public class LeilaoBLL {
    private static List<Leilao> leiloes = new ArrayList<>();

    public static List<Leilao> carregarLeiloes() {
        leiloes = ImportDal.carregarLeilao();
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

    public static List<Leilao> listarLeiloes() {
        return carregarLeiloes();
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
}
