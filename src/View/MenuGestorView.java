package View;

import BLL.UtilizadorBLL;
import Model.Utilizador;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.List;


public class MenuGestorView {
    public void exibirMenu() throws MessagingException, IOException {
        UtilizadorView utilizadorView = new UtilizadorView();
        TransacaoView transacaoView = new TransacaoView();
        EstatisticaView estatisticaView = new EstatisticaView();
        LeilaoView leilaoView = new LeilaoView();
        ProdutoView produtoView = new ProdutoView();
        TemplateView templateView = new TemplateView();

        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        List<Utilizador> lista = utilizadorBLL.listarUtilizador(0, 0);
        utilizadorView.verificarLoginsUtilizadores();
        while (true) {
            System.out.println("\n" + "=".repeat(5) + " MENU GESTOR DA LEILOEIRA " + "=".repeat(5));
            System.out.println("1. Listagem de Utilizadores");
            System.out.println("2. Aprovar Clientes");
            System.out.println("3. Inativar Clientes");
            System.out.println("4. Menu Leilões");
            System.out.println("5. Menu Produtos");
            System.out.println("6. Listagens");
            System.out.println("7. Aprovar Despósitos");
            System.out.println("8. Editar Templates");
            System.out.println("0. Sair...");
            int opcao = Tools.pedirOpcaoMenu("Escolha uma opção: ");
            switch (opcao) {
                case 1:
                    utilizadorView.exibirUtilizadores(lista);
                    break;
                case 2:
                    utilizadorView.alterarEstadoClientes(Tools.estadoUtilizador.ATIVO.getCodigo(), "Aprovar", Tools.tipoUtilizador.CLIENTE.getCodigo());
                    break;
                case 3:
                    utilizadorView.alterarEstadoClientes(Tools.estadoUtilizador.INATIVO.getCodigo(), "Inativar", Tools.tipoUtilizador.CLIENTE.getCodigo());
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
                case 8:
                    templateView.editarTemplate();
                    break;
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
