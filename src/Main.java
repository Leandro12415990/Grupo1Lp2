import Controller.AgenteController;
import DAL.AgenteDAL;
import Model.Agente;
import Utils.Tools;
import View.MenuInicialView;
import jakarta.mail.MessagingException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, MessagingException {
        AgenteController agenteController = new AgenteController();
        AgenteDAL agenteDAL = new AgenteDAL();
        Tools.agentes = agenteDAL.carregarAgentes();
        for (Agente agente: Tools.agentes)
        {

            //agenteController.lanceAgente(idLeilao, proximoLanceEsperado);
        }

        MenuInicialView menuInicialView = new MenuInicialView();
        menuInicialView.menuInicial();
    }
}
