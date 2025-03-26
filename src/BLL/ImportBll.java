package BLL;

import DAL.ImportDal;
import Model.Lance;
import Model.Leilao;
import Model.Utilizador;

import java.util.List;

public class ImportBll {

    public static List<Leilao> obterTodosLeiloes() {
        return ImportDal.carregarLeilao();
    }

    public static void listarLeilao() {
        List<Leilao> leiloes = obterTodosLeiloes();
        for (Leilao leilao : leiloes) {
            System.out.println("ID: " + leilao.getId() + " - Produto: " + leilao.getNomeProduto() + " - Estado: " + leilao.getEstado());
        }
    }

    public static List<Utilizador> obterTodosUtilizadores() {
        return ImportDal.carregarUtilizador();
    }

    public static void listarUtilizador() {
        List<Utilizador> utilizadors = obterTodosUtilizadores();
        for (Utilizador utilizador : utilizadors) {
            System.out.println("ID: " + utilizador.getId() + " - Nome: " + utilizador.getNomeUtilizador() + " - Estado: " + utilizador.getEstado());
        }
    }

    public static void gravarLeiloes(List<Leilao> leiloes) {
        ImportDal.gravarLeilao(leiloes);
    }

    public static void gravarUtilizadores(List<Utilizador> utilizadores) {
        ImportDal.gravarUtilizador(utilizadores);
    }

    public static List<Lance> obterTodosLances(){return ImportDal.carregarLance();}

    public static void listarLance(){
        List<Lance> lances = obterTodosLances();
        for (Lance lance : lances){
            System.out.print("ID: " + lance.getIdLance() + " - ID LEILAO: " + lance.getIdLeilao());
        }
    }

    public static void gravarLance(List<Lance> lances){ImportDal.gravarLance(lances);}
}
