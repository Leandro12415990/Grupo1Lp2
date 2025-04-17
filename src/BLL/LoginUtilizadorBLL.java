package BLL;

import DAL.ImportDAL;
import DAL.UtilizadorDAL;
import Model.Utilizador;
import Model.ClienteSessao;

import java.time.LocalDate;
import java.util.List;

public class LoginUtilizadorBLL {

    private List<Utilizador> utilizadores;
    private final UtilizadorDAL utilizadorDAL;

    public LoginUtilizadorBLL(UtilizadorDAL utilizadorDAL) {
        this.utilizadorDAL = utilizadorDAL;
        this.utilizadores = utilizadorDAL.carregarUtilizadores();
    }

    public Utilizador login(String email, String password) {
        for (Utilizador u : utilizadores) {
            if (email.equalsIgnoreCase(u.getEmail()) && password.equals(u.getPassword())) {
                u.setUltimoLogin(LocalDate.now());
                utilizadorDAL.gravarUtilizadores(utilizadores);
                ClienteSessao.setIdCliente(u.getId());

                int tipo = u.getTipoUtilizador();
                if (tipo == 1 || tipo == 2) return u; // 1 = GESTOR, 2 = CLIENTE (ajustar conforme o enum)
            }
        }
        return null;
    }

    public boolean lerDados() {
        this.utilizadores = utilizadorDAL.carregarUtilizadores();
        return this.utilizadores != null;
    }

    public List<Utilizador> getUtilizadores() {
        return utilizadores;
    }
}
