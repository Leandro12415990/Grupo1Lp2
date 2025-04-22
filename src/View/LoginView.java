package View;

import Controller.LoginController;
import Model.Utilizador;
import Utils.Tools;

public class LoginView {
    public Utilizador login() {
        Utilizador utilizador = null;
        LoginController loginController = new LoginController();

        System.out.println("\n" + "=".repeat(10) + " LOGIN " + "=".repeat(10));

        while (true) {
            System.out.print("Email " + Tools.alertaCancelar());
            String email = Tools.scanner.nextLine();
            if (Tools.verificarSaida(email)) return null;

            System.out.print("Password " + Tools.alertaCancelar());
            String password = Tools.scanner.nextLine();
            if (Tools.verificarSaida(password)) return null;

            utilizador = loginController.verificarLogin(email, password);

            if (utilizador != null && utilizador.getTipoUtilizador() > 0) {
                System.out.println("Login realizado com sucesso!");
                return utilizador;
            } else {
                System.out.println("Credênciais erradas ou utilizador não aprovado\n");
            }
        }
    }
}
