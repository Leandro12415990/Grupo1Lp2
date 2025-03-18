package BLL;

import Model.Utilizador;
import Utils.Tools;

import java.time.LocalDate;

public class LoginUtilizadorBll {
    public static int login(String email, String password)
    {
        for (Utilizador u : Tools.utilizadores)
        {
            if (email.equals(u.getEmail()) && password.equals(u.getPassword()))
            {
                u.setUltimoLogin(LocalDate.now());
                if (u.getTipoUtilizador() == 1) return 1;
                else if (u.getTipoUtilizador() == 2) return 2;
            }
        }
        return 0;
    }
}
