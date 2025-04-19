package Controller;

import BLL.LoginUtilizadorBLL;
import Model.Utilizador;
import Utils.Tools;

public class LoginController {
    public Utilizador verificarLogin(String email, String password)
    {
        LoginUtilizadorBLL loginUtilizadorBLL = new LoginUtilizadorBLL();
        boolean respLerDados = lerDados();
        Utilizador utilizador = null;
        if (!respLerDados) return utilizador;

        for (Utilizador u : Tools.utilizadores)
        {
            if (email.equalsIgnoreCase(u.getEmail()) && password.equals(u.getPassword()))
            {
                if (u.getEstado() == Tools.estadoUtilizador.ATIVO.getCodigo())
                {
                    utilizador = loginUtilizadorBLL.login(email, password);
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

    public boolean lerDados()
    {
        LoginUtilizadorBLL loginUtilizadorBLL = new LoginUtilizadorBLL();
        boolean respLerDados = loginUtilizadorBLL.lerDados();
        if (respLerDados) return true;
        else return false;
    }
}
