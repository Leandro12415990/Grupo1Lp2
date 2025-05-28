package Controller;

import Model.ClienteSessao;

public class LogoutController {

    private final ClienteSessao clienteSessao;

    public LogoutController(ClienteSessao clienteSessao) {
        this.clienteSessao = clienteSessao;
    }

    public void logout() {
        clienteSessao.logout();
        System.out.println("Logout realizado com sucesso.");
    }
}
