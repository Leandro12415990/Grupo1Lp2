package View;

import BLL.EmailBLL;
import BLL.RelatorioFinalBLL;
import BLL.UtilizadorBLL;
import DAL.ExcelDAL;
import DAL.TemplateDAL;
import Model.Template;
import Model.Utilizador;
import Utils.Constantes;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.time.LocalTime;

import static Utils.Tools.scanner;

public class MenuInicialView {
    public void menuInicial() throws IOException, MessagingException {
        RelatorioFinalBLL relatorioBLL = new RelatorioFinalBLL();
        String caminhoAnexo = relatorioBLL.agendarGeracaoRelatorio(LocalTime.of(21, 10,45)); // Agendar para 02:00 da manhã
        EmailBLL emailBLL = new EmailBLL();
        TemplateDAL templateDAL = new TemplateDAL();
        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        Utilizador u = utilizadorBLL.procurarUtilizadorPorId(17);
        Template template = templateDAL.carregarTemplatePorId(Constantes.templateIds.EMAIL_APROVADO);
        emailBLL.enviarEmailComAnexo(template, "pedromgp06@gmail.com", Tools.substituirTags(u, null, null), 17, caminhoAnexo);



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
