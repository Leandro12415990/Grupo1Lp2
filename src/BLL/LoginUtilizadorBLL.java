package BLL;

import DAL.ImportDal;
import Model.Utilizador;
import Utils.Tools;

import java.time.LocalDate;

public class LoginUtilizadorBLL {
    public static int login(String email, String password)
    {
        for (Utilizador u : Tools.utilizadores)
        {
            if (email.equals(u.getEmail()) && password.equals(u.getPassword()))
            {
                u.setUltimoLogin(LocalDate.now());
                ImportDal.gravarUtilizador(Tools.utilizadores);
                if (u.getTipoUtilizador() == 1) return 1;
                else if (u.getTipoUtilizador() == 2) return 2;
            }
        }
        return 0;
    }

    public static boolean lerDados()
    {
        Tools.utilizadores = ImportDal.carregarUtilizador();
        if (Tools.utilizadores != null) return true;
        return false;
    }
}
