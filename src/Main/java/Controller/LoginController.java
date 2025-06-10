package Controller;

import BLL.AgenteBLL;
import BLL.LoginUtilizadorBLL;
import Model.ClienteSessao;
import Model.Utilizador;
import Utils.Tools;

public class LoginController {

    public Utilizador verificarLogin(String email, String password) {
        LoginUtilizadorBLL loginUtilizadorBLL = new LoginUtilizadorBLL();

        boolean dadosCarregados = loginUtilizadorBLL.lerDados();
        if (!dadosCarregados) return null;

        for (Utilizador u : Tools.utilizadores) {
            if (email.equalsIgnoreCase(u.getEmail()) && password.equals(u.getPassword())) {
                if (u.getEstado() == 2) {
                    Utilizador utilizador = loginUtilizadorBLL.login(email, password);
                    Tools.clienteSessao.setIdCliente(u.getId());
                    Tools.clienteSessao.setIdTipoCliente(u.getTipoUtilizador());
                    AgenteBLL.verificarMensagensAgente(u.getId());
                    return utilizador;
                } else {
                    return null;
                }
            }
        }
        return null;
    }
}
