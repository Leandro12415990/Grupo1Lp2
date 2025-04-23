package View;

import DAL.TemplateDAL;
import Model.TemplateModel;

import java.util.Scanner;

public class TemplateView {
    private final TemplateDAL dal = new TemplateDAL();

    public void editarTemplate() {
        Scanner sc = new Scanner(System.in);

        try {
            TemplateModel template = dal.carregarTemplate();

            System.out.println("\n=== Template Atual ===");
            System.out.println("Assunto: " + template.getAssunto());
            System.out.println("Corpo:\n" + template.getCorpo());

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

            dal.guardarTemplate(template);
            System.out.println("Template atualizado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao editar template: " + e.getMessage());
        }
    }
}
