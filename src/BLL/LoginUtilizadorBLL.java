package BLL;

import Model.Utilizador;
import Utils.Tools;
import Model.ClienteSessao;

public class LoginUtilizadorBLL {
    public static Utilizador login(String email, String password) {
        for (Utilizador u : Tools.utilizadores) {
            if (u.getTipoUtilizador() == Tools.tipoUtilizador.GESTOR.getCodigo() ||
                    u.getTipoUtilizador() == Tools.tipoUtilizador.CLIENTE.getCodigo())
                return u;
            ClienteSessao.setIdCliente(u.getId());
        }
            return null;
        }

    }


