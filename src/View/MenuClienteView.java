package View;

import Model.ClienteSessao;
import Model.Utilizador;
import static Utils.Tools.scanner;

public class MenuClienteView {

    public void exibirMenu(Utilizador utilizador, UtilizadorView utilizadorView, TransacaoView transacaoView, LanceView lanceView, ClienteSessao clienteSessao) {
        while (true) {
            System.out.println("\n" + "=".repeat(5) + " MENU CLIENTE " + "=".repeat(5));
            System.out.println("1. Editar Ficha de Cliente");
            System.out.println("2. Ver os Meus Dados");
            System.out.println("3. Carteira");
            System.out.println("4. Menu Lances");
            System.out.println("0. Sair...");
            System.out.print("Escolha uma opção: ");
            System.out.println(utilizador.getId());

            int opcao = scanner.nextInt();
            scanner.nextLine().trim();

            switch (opcao) {
                case 1:
                    utilizadorView.editarCliente(utilizador);
                    break;
                case 2:
                    utilizadorView.verDadosCliente(utilizador);
                case 3:
                    transacaoView.exibirMenuTransacao();
                    break;
                case 4:
                    lanceView.exibirMenuLance();
                    break;
                case 0:
                    System.out.println("A sair...");
                    clienteSessao.logout();
                    return;
                default:
                    System.out.println("Opção inválida, tenta novamente.");
            }
        }
    }
}
