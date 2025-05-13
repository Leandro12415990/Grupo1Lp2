package BLL;

import DAL.AgenteDAL;
import DAL.UtilizadorDAL;
import Model.Agente;
import Model.Utilizador;
import Utils.Tools;

import java.time.LocalDate;
import java.util.List;

public class AgenteBLL {
    public boolean lance(int idLeilao, double proximoLanceEsperado)
    {
        int sectionID = Tools.clienteSessao.getIdCliente();
        LanceBLL lanceBLL = new LanceBLL();
        lanceBLL.adicionarLanceEletronico(idLeilao, proximoLanceEsperado, sectionID);

        return true;
    }

    public boolean criarAgente(String leilaoID) {
        AgenteDAL agenteDAL = new AgenteDAL();
        int sectionID = Tools.clienteSessao.getIdCliente();
        LocalDate data = LocalDate.now();
        Agente agente;

        Tools.agentes = agenteDAL.carregarAgentes();
        int max = -1;

        for (Agente a : Tools.agentes) {
            if (a.getIdAgente() > max) max = a.getIdAgente();
        }

        try {
            agente = new Agente(max + 1, sectionID, Integer.parseInt(leilaoID), data);
        } catch (Exception e) {
            return false;
        }

        gravarAgentes(Tools.agentes);
        return false;
    }

    public List<Agente> carregarAgentes()
    {
        AgenteDAL agenteDAL = new AgenteDAL();
        Tools.agentes = agenteDAL.carregarAgentes();
        return Tools.agentes;
    }

    public void gravarAgentes(List<Agente> agentes) {
        AgenteDAL agenteDAL = new AgenteDAL();
        agenteDAL.gravarAgente(agentes);
    }
}
