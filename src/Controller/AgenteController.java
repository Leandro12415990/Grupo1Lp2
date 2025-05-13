package Controller;

import BLL.AgenteBLL;
import DAL.AgenteDAL;
import Model.Agente;
import Utils.Tools;

public class AgenteController {
    public void lanceAgente(int idLeilao, double proximoLanceEsperado)
    {
        AgenteBLL agenteBLL = new AgenteBLL();
        boolean resp = agenteExiste(idLeilao);
        if (resp) agenteBLL.lance(idLeilao, proximoLanceEsperado);
    }

    public boolean agenteExiste(int idLeilao)
    {
        AgenteBLL agenteBLL = new AgenteBLL();
        Tools.agentes = agenteBLL.carregarAgentes();
        int sectionID = Tools.clienteSessao.getIdCliente();

        for (Agente agente: Tools.agentes)
            if (agente.getIdCliente() == sectionID && agente.getIdLeilao() == idLeilao) return true;

        return false;
    }
}
