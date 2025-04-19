package Controller;

import BLL.ImportBLL;
import Model.Lance;
import Model.Leilao;
import Model.Utilizador;
import Utils.Tools;

import java.util.List;

public class ImportController {

    public void mostrarUtilizador(int estado, int tipo) {
        ImportBLL importBLL = new ImportBLL();
        importBLL.listarUtilizador(estado, tipo);
    }

    public void gravarLeiloes(List<Leilao> leiloes) {
        ImportBLL importBLL = new ImportBLL();
        importBLL.gravarLeiloes(leiloes);
    }

    public void gravarUtilizadores(List<Utilizador> utilizadores) {
        ImportBLL importBLL = new ImportBLL();
        importBLL.gravarUtilizadores(utilizadores);
    }

    public void mostrarLance(){
        ImportBLL importBLL = new ImportBLL();
        importBLL.listarLance();
    }

    public void gravarLance(List<Lance> lances){
        ImportBLL importBLL = new ImportBLL();
        importBLL.gravarLance(lances);}
}
