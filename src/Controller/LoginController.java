package Controller;

import BLL.LoginUtilizadorBLL;
import Model.Utilizador;

public class LoginController {

    private final LoginUtilizadorBLL loginUtilizadorBLL;

    public LoginController(LoginUtilizadorBLL loginUtilizadorBLL) {
        this.loginUtilizadorBLL = loginUtilizadorBLL;
    }

    public Utilizador verificarLogin(String email, String password) {
        boolean dadosCarregados = loginUtilizadorBLL.lerDados();
        if (!dadosCarregados) return null;

        for (Utilizador u : loginUtilizadorBLL.getUtilizadores()) {
            if (email.equalsIgnoreCase(u.getEmail()) && password.equals(u.getPassword())) {
                if (u.getEstado() == 2) {
                    return loginUtilizadorBLL.login(email, password);
                } else {
                    return null;
                }
            }
        }

        return null;
    }
}
