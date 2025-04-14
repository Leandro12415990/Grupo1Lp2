package View;

import Controller.LoginController;
import Model.Utilizador;
import Utils.Tools;

public class LoginView {
    public static Utilizador login() {
        Utilizador utilizador = null;

        System.out.println("\n" + "-".repeat(7) + " LOGIN " + "-".repeat(7));

        while (true) {
            System.out.print("Email " + Tools.alertaCancelar());
            String email = Tools.scanner.nextLine();
            if (Tools.verificarSaida(email)) return null;

            System.out.print("Password " + Tools.alertaCancelar());
            String password = Tools.scanner.nextLine();
            if (Tools.verificarSaida(email)) return null;

            utilizador = LoginController.verificarLogin(email, password);

            if (utilizador != null && utilizador.getTipoUtilizador() > 0) {
                System.out.println("Login realizado com sucesso!");
                return utilizador;
            } else System.out.println("Credênciais erradas ou utilziador não aprovado\n");
        }
    }
}
