package Controller;

import BLL.LoginUtilizadorBLL;
import Model.Utilizador;
import Utils.Tools;

public class LoginController {
    public static Utilizador verificarLogin(String email, String password)
    {
        boolean respLerDados = lerDados();
        Utilizador utilizador = null;
        if (!respLerDados) return utilizador;

        for (Utilizador u : Tools.utilizadores)
        {
            if (email.equalsIgnoreCase(u.getEmail()) && password.equals(u.getPassword()))
            {
                if (u.getEstado() == Tools.estadoUtilizador.ATIVO.getCodigo())
                {
                    utilizador = LoginUtilizadorBLL.login(email, password);
                    return utilizador;
                }
                else
                {
                    return null;
                }
            }
        }

        return null;
    }

    public static boolean lerDados()
    {
        boolean respLerDados = LoginUtilizadorBLL.lerDados();
        if (respLerDados) return true;
        else return false;
    }
}
