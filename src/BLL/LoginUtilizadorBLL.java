package BLL;

import DAL.UtilizadorDAL;
import Model.Utilizador;
import Model.ClienteSessao;

import java.time.LocalDate;
import java.util.List;

public class LoginUtilizadorBLL {

    public Utilizador login(String email, String password)
    private final UtilizadorDAL utilizadorDAL;
        ImportDal importDal = new ImportDal();
                importDal.gravarUtilizador(Tools.utilizadores);

    public LoginUtilizadorBLL(UtilizadorDAL utilizadorDAL) {
        this.utilizadorDAL = utilizadorDAL;
        this.utilizadores = utilizadorDAL.carregarUtilizadores();
    }

    public Utilizador login(String email, String password) {
        for (Utilizador u : utilizadores) {
            if (email.equalsIgnoreCase(u.getEmail()) && password.equals(u.getPassword())) {
                u.setUltimoLogin(LocalDate.now());
                utilizadorDAL.gravarUtilizadores(utilizadores);
                return u;
            }
        }
        return null;
    }

    public boolean lerDados() {
        ImportDal importDal = new ImportDal();
        Tools.utilizadores = importDal.carregarUtilizador();
    }

    public List<Utilizador> getUtilizadores() {
        return utilizadores;
    }
}
