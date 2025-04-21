package BLL;

import DAL.ImportDAL;
import DAL.UtilizadorDAL;
import Model.Utilizador;
import Utils.Tools;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UtilizadorBLL {
    public List<Utilizador> listarUtilizador(int estado, int tipo) {
        ImportDal importDal = new ImportDal();
        importDal.carregarUtilizador();
        this.utilizadorDAL = utilizadorDAL;
    }

    public List<Utilizador> listarUtilizador(int estado, int tipo) {
        utilizadorDAL.carregarUtilizadores();
        List<Utilizador> utilizadoresList = new ArrayList<>();
        if (estado != 0) {
            for (Utilizador utilizador : Tools.utilizadores) {
                if (utilizador.getEstado() == estado && utilizador.getTipoUtilizador() == tipo) {
                    utilizadoresList.add(utilizador);
                }
            }
        } else {
            for (Utilizador utilizador : Tools.utilizadores) {
                if (utilizador.getTipoUtilizador() == tipo) {
                    utilizadoresList.add(utilizador);
                }
            }
        }
        return utilizadoresList;
    }

    public boolean criarCliente(String nome, String email, LocalDate nascimento, String morada, String password) {
        ImportDal importDal = new ImportDal();
        Utilizador utilizador;
        LocalDate data = LocalDate.now();

        Tools.utilizadores = importDal.carregarUtilizador();
        int max = -1;

        for (Utilizador u : Tools.utilizadores) {
            if (u.getId() > max) max = u.getId();
            if (email.equals(u.getEmail())) return false;
        }

        try {
            utilizador = new Utilizador(max + 1, nome, email, nascimento, morada, password, data, data, Tools.tipoUtilizador.CLIENTE.getCodigo(), Tools.estadoUtilizador.PENDENTE.getCodigo(), 0.0);
        } catch (Exception e) {
            return false;
        }

        Tools.utilizadores.add(utilizador);
        importDal.gravarUtilizador(Tools.utilizadores);
        return true;
    }

    public boolean aprovarCliente(Utilizador u, int estado) {
        ImportDal importDal = new ImportDal();
        int novoEstado = (estado == Tools.estadoUtilizador.ATIVO.getCodigo())
                ? Tools.estadoUtilizador.ATIVO.getCodigo()
                : Tools.estadoUtilizador.INATIVO.getCodigo();

        u.setEstado(novoEstado);

        for (Utilizador uti : Tools.utilizadores) {
            if (u.getEmail().equalsIgnoreCase(uti.getEmail()) && uti.getEstado() == novoEstado) {
                importDal.gravarUtilizador(Tools.utilizadores);
                return true;
            }
        }

        return false;
    }

    public boolean editarCliente(Utilizador utilizador, String nome, LocalDate nascimento, String morada, String password) {
        ImportDal importDal = new ImportDal();
        int soma = 0, index = 0;

        Tools.utilizadores = importDal.carregarUtilizador();

        for (Utilizador u : importDal.carregarUtilizador()) {
            if (u.getEmail().equals(utilizador.getEmail())) {
                index = soma;
                if (!nome.isEmpty()) utilizador.setNomeUtilizador(nome);
                if (!morada.isBlank()) utilizador.setMorada(morada);
                if (!password.isBlank()) utilizador.setPassword(password);
            }
            soma++;
        }

        Tools.utilizadores.set(index, utilizador);
        utilizadorDAL.gravarUtilizadores(Tools.utilizadores);
        importDal.gravarUtilizador(Tools.utilizadores);
        return true;
    }

    public void aprovarTodosClientes(Utilizador u, int estado) {
        if (estado == Tools.estadoUtilizador.ATIVO.getCodigo()) u.setEstado(Tools.estadoUtilizador.ATIVO.getCodigo());
        else u.setEstado(Tools.estadoUtilizador.INATIVO.getCodigo());
    }

    public Utilizador procurarUtilizadorPorId(int idCliente) {
        ImportDal importDal = new ImportDal();
        List<Utilizador> utilizadores = importDal.carregarUtilizador();
        for (Utilizador u : utilizadores) {
            if (u.getId() == idCliente) {
                return u;
            }
        }
        return null;
    }

    public void gravarUtilizadores(List<Utilizador> utilizadores) {
        utilizadorDAL.gravarUtilizadores(utilizadores);
    }
}
