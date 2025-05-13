package View;

import Utils.Tools;

public class ClassificacaoView {

    public int pedirClassificacao() {
        int avaliacao = 0;
        System.out.println("\nGostou do leilão? Deixe a sua avaliação.");

        do {
            System.out.print("Classificação (1 a 5): ");
            if (Tools.scanner.hasNextInt()) {
                avaliacao = Tools.scanner.nextInt();
                Tools.scanner.nextLine();
                if (avaliacao < 1 || avaliacao > 5) {
                    Tools.pedirOpcaoMenu("Por favor, insira um número válido entre 1 e 5.");
                }
            } else {
                System.out.println("Entrada inválida. Insira um número.");
                Tools.scanner.nextLine();
            }
        } while (avaliacao < 1 || avaliacao > 5);

        return avaliacao;
    }

    public String pedirComentario() {
        System.out.print("Comentário (pressione Enter para ignorar): ");
        return Tools.scanner.nextLine().trim();
    }

}
