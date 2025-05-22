package View;

import Controller.AgenteController;
import Model.Utilizador;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;

import java.sql.SQLOutput;

import static Utils.Tools.scanner;

public class MenuClienteView {
    public void exibirMenu(Utilizador utilizador) throws MessagingException, IOException {
        //
        UtilizadorView utilizadorView = new UtilizadorView();
        TransacaoView transacaoView = new TransacaoView();
        LanceView lanceView = new LanceView();
        AgenteView agenteView = new AgenteView();
        ClassificacaoView classificacaoView = new ClassificacaoView();

        utilizadorView.verificarLoginsUtilizadores();
        while (true) {
            System.out.println("\n" + "=".repeat(5) + " MENU CLIENTE " + "=".repeat(5));
            System.out.println("1. Ver os Meus Dados");
            System.out.println("2. Editar Ficha de Cliente");
            System.out.println("3. Carteira");
            System.out.println("4. Menu Lances");
            System.out.println("5. As Minhas Avaliações");
            System.out.println("6. Agente");
            System.out.println("0. Sair...");

            int opcao = Tools.pedirOpcaoMenu("Escolha uma opção: ");
            scanner.nextLine().trim();

            switch (opcao) {
                case 1:
                    utilizadorView.verDadosCliente(utilizador);
                    break;
                case 2:
                    utilizadorView.editarCliente(utilizador);
                    break;
                case 3:
                    transacaoView.exibirMenuTransacao();
                    break;
                case 4:
                    lanceView.exibirMenuLance();
                    break;
                case 5:
                    classificacaoView.exibirMenuClassificacao();
                    break;
                case 6:
                    agenteView.menuAgente();
                case 0:
                    System.out.println("A sair...");
                    Tools.clienteSessao.logout();
                    return;
                default:
                    System.out.println("Opção inválida, tenta novamente.");
            }
        }
    }
}
