package View;

import BLL.*;
import Controller.AgenteController;
import DAL.LanceDAL;
import Model.Lance;
import DAL.TemplateDAL;
import Model.Template;
import Model.Utilizador;
import Utils.Constantes;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static Utils.Tools.scanner;

public class MenuInicialView {
    public void menuInicial() throws IOException, MessagingException {
        LanceDAL lanceDAL = new LanceDAL();
        List<Lance> lances = lanceDAL.carregarLances();
        Tools.inicializarUltimoIdLance(lances);
        AgenteBLL agenteBLL = new AgenteBLL();
        agenteBLL.iniciarMonitorizacaoDinamica();
        RelatorioFinalBLL relatorioBLL = new RelatorioFinalBLL();
        relatorioBLL.agendarGeracaoRelatorio(LocalTime.of(22, 0));

        LoginView loginView = new LoginView();
        MenuClienteView menuClienteView = new MenuClienteView();
        MenuGestorView menuGestorView = new MenuGestorView();
        UtilizadorView utilizadorView = new UtilizadorView();
        TESTEpp Testepp = new TESTEpp();

        int opcao;
        do {
            System.out.println("\nBem-vindo à leiloeira Valor em Alta!\n");

            System.out.println("1. Efetuar Login");
            System.out.println("2. Efetuar Registo");
            System.out.println("0. Sair...");

            opcao = Tools.pedirOpcaoMenu("Escolha uma opção: ");
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    Utilizador utilizador = loginView.login();

                    if (utilizador == null) {
                        System.out.println("Erro a fazer Login");
                    } else if (utilizador.getTipoUtilizador() == Tools.tipoUtilizador.GESTOR.getCodigo()) {
                        menuGestorView.exibirMenu();
                    } else if (utilizador.getTipoUtilizador() == Tools.tipoUtilizador.CLIENTE.getCodigo()) {
                        menuClienteView.exibirMenu(utilizador);
                    }
                    break;
                case 2:
                    utilizadorView.registarCliente();
                    break;
                case 3:
                    Testepp.importarUtilizadores();
                    break;
                case 0:
                    System.out.println("A desligar sistema...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        } while (true);

    }
}
