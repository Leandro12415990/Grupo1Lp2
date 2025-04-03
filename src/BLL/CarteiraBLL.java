package BLL;

import DAL.ImportDal;
import Model.Carteira;
import Model.Leilao;

import java.util.ArrayList;
import java.util.List;

public class CarteiraBLL {
    private static List<Carteira> carteiraList = new ArrayList<>();

    public static List<Carteira> carregarCarteira() {
        carteiraList = ImportDal.carregarCarteira();
        return carteiraList;
    }

    public static void criarDeposito(Carteira carteira) {
        carregarCarteira();
        carteira.setIdDeposito(verificarUltimoIdCarteira(carteiraList) + 1);
        carteiraList.add(carteira);
        ImportDal.gravarCarteira(carteiraList);
    }

    private static int verificarUltimoIdCarteira(List<Carteira> carteiraList) {
        int ultimoId = 0;
        for (Carteira carteira : carteiraList) {
            if (carteira.getIdDeposito() > ultimoId) {
                ultimoId = carteira.getIdDeposito();
            }
        }
        return ultimoId;
    }

}
