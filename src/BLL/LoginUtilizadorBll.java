package BLL;

import DAL.ImportDal;
import Model.Utilizador;
import Utils.Tools;
import Model.ClienteSessao;

import java.time.LocalDate;

public class LoginUtilizadorBll {
    public static int login(String email, String password) {
        for (Utilizador u : Tools.utilizadores) {
            if (email.equalsIgnoreCase(u.getEmail()) && password.equals(u.getPassword())) {
                u.setUltimoLogin(LocalDate.now());
                ImportDal.gravarUtilizador(Tools.utilizadores);

                ClienteSessao.setIdCliente(u.getId());
                if (u.getTipoUtilizador() == 1) return 1;  // Administrador
                else if (u.getTipoUtilizador() == 2) return 2;  // Cliente
            }
        }
        return 0;
    }

    public static boolean lerDados() {
        Tools.utilizadores = ImportDal.carregarUtilizador();
        return Tools.utilizadores != null;
    }
}

