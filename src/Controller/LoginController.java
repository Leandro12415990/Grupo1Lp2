package Controller;

import BLL.LoginUtilizadorBll;
import Model.Utilizador;
import Utils.Tools;

public class LoginController {
    public static boolean verificarLogin(String email, String password)
    {
        for (Utilizador u : Tools.utilizadores)
        {
            if (email.equals(u.getEmail()) && password.equals(u.getPassword()))
            {
                LoginUtilizadorBll.login(email, password);
                return true;
            }
        }

        return false;
    }
}
