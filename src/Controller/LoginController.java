package Controller;

import BLL.LoginUtilizadorBLL;
import Model.Utilizador;
import Model.ClienteSessao; // Importando a classe ClienteSessao
import Utils.Tools;

public class LoginController {

    public Utilizador verificarLogin(String email, String password) {
        LoginUtilizadorBLL loginUtilizadorBLL = new LoginUtilizadorBLL();
        ClienteSessao clienteSessao = new ClienteSessao();

        boolean dadosCarregados = loginUtilizadorBLL.lerDados();
        if (!dadosCarregados) return null;

        for (Utilizador u : Tools.utilizadores) {
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
