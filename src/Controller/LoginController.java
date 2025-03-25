package Controller;

import BLL.LoginUtilizadorBll;
import Model.Utilizador;
import Utils.Tools;

public class LoginController {
    public static int verificarLogin(String email, String password)
    {
        boolean respLerDados = lerDados();
        int tipoUtilizador = 0;
        if (!respLerDados) return tipoUtilizador;

        for (Utilizador u : Tools.utilizadores)
        {
            if (email.equals(u.getEmail()) && password.equals(u.getPassword()))
            {
                tipoUtilizador = LoginUtilizadorBll.login(email, password);
                return tipoUtilizador;
            }
        }

        return tipoUtilizador;
    }

    public static boolean lerDados()
    {
        boolean respLerDados = LoginUtilizadorBll.lerDados();
        if (respLerDados) return true;
        else return false;
    }
}
