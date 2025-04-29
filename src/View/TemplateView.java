package View;

import Controller.TemplateController;
import Model.Template;
import Utils.Constantes;
import Utils.Tools;

import java.util.Scanner;

public class TemplateView {
    private final TemplateController controller = new TemplateController();

    public void editarTemplate() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\nEscolha o template para editar:");
        System.out.println("1 - " + Tools.tipoEmail.fromCodigo(1).name());
        System.out.println("2 - " + Tools.tipoEmail.fromCodigo(2).name());
        System.out.println("3 - " + Tools.tipoEmail.fromCodigo(3).name());
        System.out.println("4 - " + Tools.tipoEmail.fromCodigo(4).name());
        System.out.print("Digite o número do template: ");

        int escolha = sc.nextInt();
        sc.nextLine();

        String idTemplate = obterIdTemplatePorEscolha(escolha);
        if (idTemplate == null) {
            System.out.println("Escolha inválida!");
            return;
        }

        try {
            Template template = controller.obterTemplatePorId(idTemplate);

            if (template == null) {
                System.out.println("Template com ID " + idTemplate + " não encontrado.");
                return;
            }

            System.out.println("\n=== Template Atual ===");
            System.out.println("Assunto: " + template.getAssunto());
            System.out.println("Corpo:\n" + template.getCorpo());

            exibirTagsDisponiveis();
            System.out.print("\nNovo assunto (ENTER para manter): ");
            String novoAssunto = sc.nextLine();
            if (!novoAssunto.isBlank()) {
                template.setAssunto(novoAssunto);
            }

            System.out.println("Novo corpo da mensagem (digite 'FIM' para terminar):");
            StringBuilder novoCorpo = new StringBuilder();
            while (true) {
                String linha = sc.nextLine();
                if (linha.equalsIgnoreCase("FIM")) break;
                novoCorpo.append(linha).append("\n");
            }
            if (!novoCorpo.toString().isBlank()) {
                template.setCorpo(novoCorpo.toString().trim());
            }

            controller.guardarTemplate(idTemplate, template);
            System.out.println("Template atualizado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao editar template: " + e.getMessage());
        }
    }

    private String obterIdTemplatePorEscolha(int escolha) {
        switch (escolha) {
            case 1:
                return Constantes.templateIds.EMAIL_REGISTO;
            case 2:
                return Constantes.templateIds.EMAIL_VENCEDOR_LEILAO;
            case 3:
                return Constantes.templateIds.EMAIL_CLIENTE_OFFLINE;
            case 4:
                return Constantes.templateIds.EMAIL_SEM_CREDITOS;
            default:
                return null;
        }
    }

    private void exibirTagsDisponiveis() {
        System.out.println("\n=== Tags Disponíveis ===");
        System.out.println("{NOME} - Nome do Cliente");
        System.out.println("{EMAIL} - Email do Cliente");
        System.out.println("{DATA} - Data Atual");
        System.out.println("{NOME_PRODUTO} - Nome do Produto");
        System.out.println("{TIPO_LEILAO} - Tipo do Leilao");
        System.out.println("{NOME_lEILAO} - Nome do Leilao");
        System.out.println("{SALDO} - Saldo Atual");
        System.out.println("{EQUIPA} - Equipa do Leilão");
    }
}
