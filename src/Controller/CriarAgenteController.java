package Controller;

import BLL.CriarAgenteBLL;

public class CriarAgenteController {
    public void criarAgente(String leilaoID) {
        CriarAgenteBLL criarAgenteBLL = new CriarAgenteBLL();
        criarAgenteBLL.criarAgente(leilaoID);
    }
}
