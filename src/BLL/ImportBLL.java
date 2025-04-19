package BLL;

import DAL.ImportDal;
import Model.Lance;
import Model.Leilao;
import Model.Utilizador;
import Utils.Tools;

import java.util.List;

public class ImportBLL {

    public List<Leilao> obterTodosLeiloes() {
        ImportDal importDal = new ImportDal();
        return importDal.carregarLeilao();
    }

    public List<Utilizador> obterTodosUtilizadores() {
        ImportDal importDal = new ImportDal();
        return importDal.carregarUtilizador();
    }

    public void listarUtilizador(int estado, int tipo) {
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

    public void gravarLeiloes(List<Leilao> leiloes) {
        ImportDal importDal = new ImportDal();
        importDal.gravarLeilao(leiloes);
    }

    public void gravarUtilizadores(List<Utilizador> utilizadores) {
        ImportDal importDal = new ImportDal();
        importDal.gravarUtilizador(utilizadores);
    }

    public List<Lance> obterTodosLances() {
        ImportDal importDal = new ImportDal();
        return importDal.carregarLance();
    }

    public void listarLance(){
        List<Lance> lances = obterTodosLances();
        for (Lance lance : lances){
            System.out.print("ID: " + lance.getIdLance() + " - ID LEILAO: " + lance.getIdLeilao());
        }
    }

    public void gravarLance(List<Lance> lances) {
        ImportDal importDal = new ImportDal();
        importDal.gravarLance(lances);
    }
}
