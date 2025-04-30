package View;

import Utils.Tools;

public class ClassificacaoView {

    public int pedirClassificacao() {
        int nota = 0;
        do {
            System.out.print("Classifique o leilão (1 a 5): ");
            if (Tools.scanner.hasNextInt()) {
                nota = Tools.scanner.nextInt();
                Tools.scanner.nextLine();
            } else {
                Tools.scanner.nextLine();
                Tools.pedirOpcaoMenu("Entrada inválida. Insira um número.");
                continue;
            }
        } while (nota < 1 || nota > 5);

        return nota;
    }

    public String pedirComentario() {
        System.out.print("Comentário (pressione Enter para ignorar): ");
        return Tools.scanner.nextLine().trim();
    }
}
