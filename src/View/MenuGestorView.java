package View;

import Model.Transacao;
import Utils.Tools;

import static Utils.Tools.scanner;

public class MenuGestorView {
    public static void exibirMenu() {
        while (true) {
            System.out.println("\n"+"=".repeat(5) + " MENU GESTOR DA LEILOEIRA " + "=".repeat(5));
            System.out.println("1. Listagem de Utilizadores");
            System.out.println("2. Aprovar Clientes");
            System.out.println("3. Inativar Clientes");
            System.out.println("4. Aprovar Depósitos");
            System.out.println("5. Menu Produtos");
            System.out.println("6. Menu Leilões");
            System.out.println("7. Listagens");
            System.out.println("0. Sair...");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine().trim();
            switch (opcao) {
                case 1:
                    ImportView.mostrarUtilizador(Tools.estadoUtilizador.getDefault().getCodigo(), 2);
                    break;
                case 2:
                    AprovarClienteView.exibirMenu(Tools.estadoUtilizador.ATIVO.getCodigo());
                    break;
                case 3:
                    AprovarClienteView.exibirMenu(Tools.estadoUtilizador.INATIVO.getCodigo());
                    break;
                case 4:
                    TransacaoView.exibirMenuTransacao();
                    break;
                case 5:
                    ProdutoView.exibirProduto();
                    break;
                case 6:
                    LeilaoView.exibirMenuLeiloes();
                    break;
                case 7:
                    EstatisticaView.exibirMenuListagem();
                    break;
                case 0:
                    System.out.println("A desligar sistema...");
                    return;
                default:
                    System.out.println("Opção inválida, tenta novamente.");
            }
        }
    }
}
