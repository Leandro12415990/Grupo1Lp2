package View;

import BLL.RelatorioFinalBLL;
import DAL.ExcelDAL;
import Model.Utilizador;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.time.LocalTime;

import static Utils.Tools.scanner;

public class MenuInicialView {
    public void menuInicial() throws IOException, MessagingException {
        RelatorioFinalBLL relatorioBLL = new RelatorioFinalBLL();
        relatorioBLL.agendarGeracaoRelatorio(LocalTime.of(22, 0)); // Agendar para 02:00 da manhã
        LoginView loginView = new LoginView();
        MenuClienteView menuClienteView = new MenuClienteView();
        MenuGestorView menuGestorView = new MenuGestorView();
        UtilizadorView utilizadorView = new UtilizadorView();
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
