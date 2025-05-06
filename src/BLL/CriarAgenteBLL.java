package BLL;

import DAL.AgenteDAL;
import Model.Agente;
import Model.ClienteSessao;
import Model.Utilizador;
import Utils.Tools;

import java.time.LocalDate;

public class CriarAgenteBLL {
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

        Tools.agentes.add(agente);
        agenteDAL.gravarAgente(Tools.agentes);
        return false;
    }
}
