package View;
import Utils.Tools;
import Controller.RegistarClienteController;

import java.net.PasswordAuthentication;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RegistarClienteView {
    private void MenuRegistarCliente()
    {
        System.out.println("----------Registar----------");
        System.out.println("Nome:");
        String nome = Tools.scanner.nextLine();
        System.out.println("Morada");
        String morada = Tools.scanner.nextLine();
        System.out.println("Data de Nascimento");
        LocalDate nascimento = LocalDate.parse(Tools.scanner.nextLine());
        System.out.println("E-mail");
        String email = Tools.scanner.nextLine();
        System.out.println("Insira a Password");
        String passwordFirst = Tools.scanner.nextLine();
        System.out.println("Repita a Password");
        String passwordSecound = Tools.scanner.nextLine();
    }
}
