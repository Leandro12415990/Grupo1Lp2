package Controller;

import BLL.AgenteBLL;

public class CriarAgenteController {
    public void criarAgente(String leilaoID) {
        AgenteBLL agenteBLL = new AgenteBLL();
        agenteBLL.criarAgente(leilaoID);
    }
}
