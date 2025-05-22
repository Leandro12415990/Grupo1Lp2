package Controller;

import BLL.AgenteBLL;
import Model.Agente;
import View.AgenteView;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.List;

public class AgenteController {
    private final AgenteBLL agenteBLL;
    private AgenteView agenteView;

    public AgenteController() {
        this.agenteBLL = new AgenteBLL();
    }

    public void setAgenteView(AgenteView view){
        this.agenteView = view;
    }

    public void iniciarMenu() throws MessagingException, IOException {
        agenteView.menuAgente();
    }

    public boolean criarAgente(Agente agente) {
        return agenteBLL.adicionarAgente(agente);
    }

    public boolean eliminarAgente(int idLeilao, int idCliente) {
        return agenteBLL.removerAgente(idLeilao, idCliente);
    }

    public int obterProximoId() {
        return agenteBLL.obterProximoId();
    }

    public List<Agente> listarAgentes() {
        return agenteBLL.listarAgentes();
    }
}
