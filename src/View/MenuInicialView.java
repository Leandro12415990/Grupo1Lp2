package View;

import Model.ClienteSessao;
import Model.Utilizador;
import Utils.Tools;

import static Utils.Tools.scanner;

public class MenuInicialView {
    public void menuInicial() {
        LoginView loginView = new LoginView();
        MenuClienteView menuClienteView = new MenuClienteView();
        UtilizadorView utilizadorView = new UtilizadorView();
        int opcao;
        do {
            System.out.println("\nBem-vindo à leiloeira Valor em Alta!\n");

            System.out.println("1. Efetuar Login");
            System.out.println("2. Efetuar Registo");
            System.out.println("0. Sair...");

            opcao = Tools.pedirOpcaoMenu("Escolha uma opção: ");
            scanner.nextLine().trim();

            switch (opcao) {
                case 1:
                    Utilizador utilizador = loginView.login();

                    if (utilizador == null) {
                        System.out.println("Erro a fazer Login");
                    } else if (utilizador.getTipoUtilizador() == Tools.tipoUtilizador.GESTOR.getCodigo()) {
                        menuGestorView.exibirMenu(utilizadorView, leilaoView, produtoView, estatisticaView, transacaoView, clienteSessao);
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
        } while (opcao != 0);
    }
}
