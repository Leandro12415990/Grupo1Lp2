package Controller;

import BLL.LanceBLL;
import Model.Lance;
import View.LanceView;

import java.util.List;

public class LanceController {
    /*public static void listarLance(){
        List<Lance> lances = LanceBLL.listarLance();

        LanceView.exibirLance(lances);
    }*/

    public static Lance procurarLanceId(int id){
        if (id > 0){
            return LanceBLL.procurarLanceId(id);
        }
        return null;
    }

    public static void mostrarLancesPorLeilao(int idLeilao) {
        List<Lance> lances = LanceBLL.obterLancesPorLeilao(idLeilao);

        if (lances.isEmpty()) {
            System.out.println("Não há lances para este leilão.");
        } else {
            System.out.println("\nLances para o Leilão " + idLeilao + ":");
            for (Lance lance : lances) {
                System.out.println(lance);
            }
        }
    }

}
