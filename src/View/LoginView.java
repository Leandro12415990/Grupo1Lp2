package View;

import Controller.LoginController;
import Utils.Tools;

public class LoginView {
    public static void login()
    {
        System.out.println("=== Login ===");
        System.out.println("Email:");
        String email = Tools.scanner.nextLine();
        System.out.println("Password:");
        String password = Tools.scanner.nextLine();
        boolean resp = LoginController.verificarLogin(email, password);

        if (resp) System.out.println("Login realizado com sucesso!");
        else System.out.println("Email ou Password inválidos!");
    }
}
