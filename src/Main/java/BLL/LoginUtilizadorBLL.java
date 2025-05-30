package BLL;

import DAL.UtilizadorDAL;
import Model.Utilizador;
import Utils.Tools;

import java.time.LocalDate;

public class LoginUtilizadorBLL {

    public Utilizador login(String email, String password) {
        UtilizadorDAL utilizadorDAL = new UtilizadorDAL();
        for (Utilizador u : Tools.utilizadores) {
            if (email.equalsIgnoreCase(u.getEmail()) && password.equals(u.getPassword())) {
                u.setUltimoLogin(LocalDate.now());
                utilizadorDAL.gravarUtilizadores(Tools.utilizadores);

                Tools.clienteSessao.setIdCliente(u.getId());

                if (u.getTipoUtilizador() == Tools.tipoUtilizador.GESTOR.getCodigo() || u.getTipoUtilizador() == Tools.tipoUtilizador.CLIENTE.getCodigo())
                    return u;
            }
        }
        return null;
    }

    public boolean lerDados() {
        UtilizadorDAL utilizadorDAL = new UtilizadorDAL();
        Tools.utilizadores = utilizadorDAL.carregarUtilizadores();
        return Tools.utilizadores != null;
    }
}
