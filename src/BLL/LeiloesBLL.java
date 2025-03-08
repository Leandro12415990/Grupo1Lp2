package BLL;

import Model.Leilao;
import DAL.ImportDal;
import Utils.Tools;

import java.util.ArrayList;
import java.util.List;

import static Utils.Tools.FORMATTER;

public class LeiloesBLL {
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

    private static List<Leilao> obterTodosLeiloes() {
        return ImportDal.carregarLeilao();
    }

    // Retorna os dados para a Controller manipular e exibir na View
    public static List<Leilao> listarLeiloes() {
        return obterTodosLeiloes();
    }
}
