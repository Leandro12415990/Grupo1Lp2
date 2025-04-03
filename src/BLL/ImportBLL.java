package BLL;

import DAL.ImportDal;
import Model.Lance;
import Model.Leilao;
import Model.Utilizador;
import Utils.Tools;

import java.util.List;

public class ImportBLL {

    public static List<Leilao> obterTodosLeiloes() {
        return ImportDal.carregarLeilao();
    }

    public static List<Utilizador> obterTodosUtilizadores() {
        return ImportDal.carregarUtilizador();
    }

    public static void listarUtilizador(int estado, int tipo) {
        List<Utilizador> utilizadors = obterTodosUtilizadores();
        if (estado == Tools.estadoUtilizador.getDefault().getCodigo()) {
            for (Utilizador utilizador : utilizadors) {
                if(tipo != 0 && utilizador.getTipoUtilizador() == tipo) System.out.println("ID: " + utilizador.getId() + " - Nome: " + utilizador.getNomeUtilizador() + " - Email: " + utilizador.getEmail() + " - Estado: " + utilizador.getEstado());
                else System.out.println("ID: " + utilizador.getId() + " - Nome: " + utilizador.getNomeUtilizador() + " - Email: " + utilizador.getEmail() + " - Estado: " + utilizador.getEstado());
            }
        }
        else if (estado == Tools.estadoUtilizador.ATIVO.getCodigo()) {
            for (Utilizador utilizador : utilizadors) {
                if (utilizador.getEstado() == Tools.estadoUtilizador.ATIVO.getCodigo() && (tipo != 0 && utilizador.getTipoUtilizador() == tipo))
                    System.out.println("ID: " + utilizador.getId() + " - Nome: " + utilizador.getNomeUtilizador() + " - Email: " + utilizador.getEmail() + " - Estado: " + utilizador.getEstado());
                else if (utilizador.getEstado() == Tools.estadoUtilizador.ATIVO.getCodigo())
                    System.out.println("ID: " + utilizador.getId() + " - Nome: " + utilizador.getNomeUtilizador() + " - Email: " + utilizador.getEmail() + " - Estado: " + utilizador.getEstado());
            }
        }
        else if (estado == Tools.estadoUtilizador.PENDENTE.getCodigo()) {
            for (Utilizador utilizador : utilizadors) {
                if (utilizador.getEstado() == Tools.estadoUtilizador.PENDENTE.getCodigo() && (tipo != 0 && utilizador.getTipoUtilizador() == tipo))
                    System.out.println("ID: " + utilizador.getId() + " - Nome: " + utilizador.getNomeUtilizador() + " - Email: " + utilizador.getEmail() + " - Estado: " + utilizador.getEstado());
                else if (utilizador.getEstado() == Tools.estadoUtilizador.PENDENTE.getCodigo())
                    System.out.println("ID: " + utilizador.getId() + " - Nome: " + utilizador.getNomeUtilizador() + " - Email: " + utilizador.getEmail() + " - Estado: " + utilizador.getEstado());
            }
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
            System.out.print("ID: " + lance.getIdLance() + " - ID LEILAO: " + lance.getIdLeilao() + " - NOME CLIENTE: " + lance.getNomeCliente());
        }
    }

    public static void gravarLance(List<Lance> lances){ImportDal.gravarLance(lances);}
}
