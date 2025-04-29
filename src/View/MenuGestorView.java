package View;

import BLL.EmailBLL;
import BLL.UtilizadorBLL;
import DAL.TemplateDAL;
import Model.TemplateModel;
import Model.Utilizador;
import Utils.Tools;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MenuGestorView {
    public void exibirMenu() throws IOException, MessagingException {
        UtilizadorView utilizadorView = new UtilizadorView();
        TransacaoView transacaoView = new TransacaoView();
        EstatisticaView estatisticaView = new EstatisticaView();
        LeilaoView leilaoView = new LeilaoView();
        ProdutoView produtoView = new ProdutoView();

        UtilizadorBLL utilizadorBLL = new UtilizadorBLL();
        List<Utilizador> lista = utilizadorBLL.listarUtilizador(0, 0);

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
                    TemplateView view = new TemplateView();
                    view.editarTemplate();
                    break;
                case 9:
                    System.out.println("\nEscolha o template para enviar:");
                    System.out.println("1 - " + Tools.tipoEmail.fromCodigo(1).name());
                    System.out.println("2 - " + Tools.tipoEmail.fromCodigo(2).name());
                    System.out.println("3 - " + Tools.tipoEmail.fromCodigo(3).name());
                    System.out.println("4 - " + Tools.tipoEmail.fromCodigo(4).name());
                    System.out.print("Digite o número do template: ");
                    idTemplate = Tools.scanner.nextLine().trim();

                    if (idTemplate.isEmpty()) {
                        System.out.println("ID do template não pode ser vazio.");
                        break;
                    }

                    TemplateDAL dal = new TemplateDAL();
                    TemplateModel template = dal.carregarTemplatePorId(idTemplate);

                    if (template == null) {
                        System.out.println("Template com ID " + idTemplate + " não encontrado.");
                        break;
                    }

                    Map<String, String> variaveis = new HashMap<>();
                    variaveis.put("nome", "Exemplo Nome");
                    variaveis.put("email", "exemplo@dominio.com");

                    EmailBLL emailBLL = new EmailBLL();
                    emailBLL.enviarEmail(template, "oliveira4797@gmail.com", variaveis);

                    System.out.println("Email enviado com sucesso!");
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
