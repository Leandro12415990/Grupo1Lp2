package Controller;

import BLL.ImportBLL;
import Model.Lance;
import Model.Leilao;
import Model.Utilizador;
import Utils.Tools;

import java.util.List;

public class ImportController {

    public static void mostrarUtilizador(int estado) {
        ImportBLL.listarUtilizador(estado);
    }

    public static void gravarLeiloes(List<Leilao> leiloes) {
        ImportBLL.gravarLeiloes(leiloes);
    }

    public static void gravarUtilizadores(List<Utilizador> utilizadores) {
        ImportBLL.gravarUtilizadores(utilizadores);
    }

    public static void mostrarLance(){
        ImportBLL.listarLance();}

    public static void gravarLance(List<Lance> lances){
        ImportBLL.gravarLance(lances);}
}
