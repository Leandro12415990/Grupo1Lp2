package View;

import BLL.EmailBLL;
import DAL.TemplateDAL;
import Model.TemplateModel;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MenuGestorView {
    public void exibirMenu() throws IOException, MessagingException {
        UtilizadorView utilizadorView = new UtilizadorView();
        TransacaoView transacaoView = new TransacaoView();
        EstatisticaView estatisticaView = new EstatisticaView();
        LeilaoView leilaoView = new LeilaoView();
        ProdutoView produtoView = new ProdutoView();

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
            System.out.println("9. Enviar Email");
            System.out.println("0. Sair...");
            System.out.print("Escolha uma opção: ");

            int opcao = Tools.scanner.nextInt();
            Tools.scanner.nextLine().trim();
            String idTemplate = "";
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
                case 8:
                    TemplateView view = new TemplateView();
                    view.editarTemplate(); // sem parâmetros
                    break;
                case 9:
                    TemplateDAL dal = new TemplateDAL();
                    TemplateModel template = dal.carregarTemplatePorId(idTemplate);

                    // 2. Preenche as variáveis a substituir
                    Map<String, String> variaveis = new HashMap<>();
                    variaveis.put("nome", String.valueOf(Tools.clienteSessao.getIdCliente()));  // Conversão de int para String
                    variaveis.put("email", "o Sandro é gay");


                    // 3. Envia o e-mail
                    EmailBLL emailBLL = new EmailBLL();
                    emailBLL.enviarEmail(template, "oliveira4797@gmail.com", variaveis);
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
