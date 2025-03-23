package Controller;

import BLL.ImportBll;
import Model.Lance;
import Model.Leilao;
import Model.Utilizador;

import java.util.List;

public class ImportController {

    public static void mostrarUtilizador() {
        ImportBll.listarUtilizador();
    }

    public static void gravarLeiloes(List<Leilao> leiloes) {
        ImportBll.gravarLeiloes(leiloes);
    }

    public static void gravarUtilizadores(List<Utilizador> utilizadores) {
        ImportBll.gravarUtilizadores(utilizadores);
    }

    public static void mostrarLance(){ImportBll.listarLance();}

    public static void gravarLance(List<Lance> lances){ImportBll.gravarLance(lances);}
}
