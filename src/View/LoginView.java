package View;

import Controller.LoginController;
import Model.Utilizador;
import Utils.Tools;

public class LoginView {
    public static int login() {
        int tipoUtilizador = 0;

        System.out.println("\n" + "-".repeat(7) + " LOGIN " + "-".repeat(7));

        while (true) {
            System.out.print("Email (-1 para cancelar): ");
            String email = Tools.scanner.nextLine();
            if (email.equals("-1")) {
                System.out.println("Voltando ao menu anterior...");
                return -1;
            }

            System.out.print("Password (-1 para cancelar): ");
            String password = Tools.scanner.nextLine();
            if (password.equals("-1")) {
                System.out.println("Voltando ao menu anterior...");
                return -1;
            }

            tipoUtilizador = LoginController.verificarLogin(email, password);

            if (tipoUtilizador > 0) {
                System.out.println("Login realizado com sucesso!");
                return tipoUtilizador;
            } else if (tipoUtilizador == -1) {
                System.out.println("O seu utilizador ainda não foi aprovado.\n");
            } else {
                System.out.println("Credênciais erradas ou utilizador não aprovado\n");
            }
        }
    }
}
