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
        carteiraList.add(carteira);
        ImportDal.gravarCarteira(carteiraList);
    }
    

}
