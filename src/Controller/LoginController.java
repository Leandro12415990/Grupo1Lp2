package Controller;

import BLL.LoginUtilizadorBLL;
import Model.Utilizador;
import Model.ClienteSessao; // Importando a classe ClienteSessao

public class LoginController {

    private final LoginUtilizadorBLL loginUtilizadorBLL;
    private final ClienteSessao clienteSessao;

    public LoginController(LoginUtilizadorBLL loginUtilizadorBLL, ClienteSessao clienteSessao) {
        this.loginUtilizadorBLL = loginUtilizadorBLL;
        this.clienteSessao = clienteSessao;
    }

    public Utilizador verificarLogin(String email, String password) {
        boolean dadosCarregados = loginUtilizadorBLL.lerDados();
        if (!dadosCarregados) return null;

        for (Utilizador u : loginUtilizadorBLL.getUtilizadores()) {
            if (email.equalsIgnoreCase(u.getEmail()) && password.equals(u.getPassword())) {
                if (u.getEstado() == 2) {
                    Utilizador utilizador = loginUtilizadorBLL.login(email, password);

                    clienteSessao.setIdCliente(u.getId());
                    return utilizador;
                } else {
                    return null;
                }
            }
        }
        return null;
    }
}
