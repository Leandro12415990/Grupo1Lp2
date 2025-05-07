package View;

import Controller.ClassificacaoController;
import Model.Leilao;
import Utils.Tools;

public class ClassificacaoView {

    public void pedirClassificacao(Leilao leilao) {
        int nota = 0;
        System.out.println("\nGostou do leilão: " + leilao.getDescricao() + "? Deixe a sua avaliação.");

        do {
            nota = Tools.pedirOpcaoMenu("Insira uma classificação (1 a 5)" + Tools.alertaCancelar());


                if (nota == -1) {
                    System.out.println("Classificação cancelada.");
                    return;
                }

                if (nota < 1 || nota > 5) {
                   nota = Tools.pedirOpcaoMenu("Por favor, insira um número válido entre 1 e 5." + Tools.alertaCancelar());
                }
        } while (nota < 1 || nota > 5);

        ClassificacaoController controller = new ClassificacaoController();
        int idCliente = Tools.clienteSessao.getIdCliente();

        System.out.print("Insira um comentário: ");
        Tools.scanner.nextLine();
        String comentario = Tools.scanner.nextLine().trim();


        controller.registarAvaliacao(idCliente, leilao.getId(), nota, comentario);
        System.out.println("Obrigado pela sua avaliação!");
    }
}
