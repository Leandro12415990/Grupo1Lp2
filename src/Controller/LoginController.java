package Controller;

import Model.Utilizador;
import Utils.Tools;

import java.time.LocalDate;

public class LoginController {
    public static boolean verificarLogin(String email, String password)
    {
        for (Utilizador u : Tools.utilizadores)
        {
            if (email.equals(u.getEmail()) && password.equals(u.getPassword()))
            {
                u.setUltimoLogin(LocalDate.now());
                return true;
            }
        }

        return false;
    }
}
