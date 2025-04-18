package View;

import Utils.Tools;


public class MenuGestorView {
    public void exibirMenu(UtilizadorView utilizadorView, LeilaoView leilaoView, ProdutoView produtoView, EstatisticaView estatisticaView, TransacaoView transacaoView) {
        while (true) {
            System.out.println("\n" + "=".repeat(5) + " MENU GESTOR DA LEILOEIRA " + "=".repeat(5));
            System.out.println("1. Listagem de Utilizadores");
            System.out.println("2. Aprovar Clientes");
            System.out.println("3. Inativar Clientes");
            System.out.println("4. Menu Leilões");
            System.out.println("5. Menu Produtos");
            System.out.println("6. Listagens");
            System.out.println("7. Aprovar Despósitos");
            System.out.println("0. Sair...");
            System.out.print("Escolha uma opção: ");

            int opcao = Tools.scanner.nextInt();
            Tools.scanner.nextLine().trim();
            switch (opcao) {
                case 1:
                    utilizadorView.mostrarUtilizador(Tools.estadoUtilizador.getDefault().getCodigo(), 2);
                    break;
                case 2:
                    utilizadorView.aprovarCliente(Tools.estadoUtilizador.ATIVO.getCodigo());
                    break;
                case 3:
                    utilizadorView.aprovarCliente(Tools.estadoUtilizador.INATIVO.getCodigo());
                    break;
                case 4:
                    leilaoView.exibirMenuLeiloes();
                    break;
                case 5:
                    produtoView.exibirProduto();
                    break;
                case 6:
                    estatisticaView.exibirMenuListagem();
                    break;
                case 7:
                    transacaoView.aprovarDepositos();
                    break;
                case 0:
                    System.out.println("A sair...");
                    return;
                default:
                    System.out.println("Opção inválida, tenta novamente.");
            }
        }
    }
}
